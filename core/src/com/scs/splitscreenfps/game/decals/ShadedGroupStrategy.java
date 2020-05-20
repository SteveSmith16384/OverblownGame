/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.scs.splitscreenfps.game.decals;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

/** <p>
 * Minimalistic grouping strategy that splits decals into opaque and transparent ones enabling and disabling blending as needed.
 * Opaque decals are rendered first (decal color is ignored in opacity check).<br/>
 * Use this strategy only if the vast majority of your decals are opaque and the few transparent ones are unlikely to overlap.
 * </p>
 * <p>
 * Can produce invisible artifacts when transparent decals overlap each other.
 * </p>
 * <p>
 * Needs to be explicitly disposed as it might allocate a ShaderProgram when GLSL 2.0 is used.
 * </p>
 * <p>
 * States (* = any, EV = entry value - same as value before flush):<br/>
 * <table>
 * <tr>
 * <td></td>
 * <td>expects</td>
 * <td>exits on</td>
 * </tr>
 * <tr>
 * <td>glDepthMask</td>
 * <td>true</td>
 * <td>EV</td>
 * </tr>
 * <tr>
 * <td>GL_DEPTH_TEST</td>
 * <td>enabled</td>
 * <td>EV</td>
 * </tr>
 * <tr>
 * <td>glDepthFunc</td>
 * <td>GL_LESS | GL_LEQUAL</td>
 * <td>EV</td>
 * </tr>
 * <tr>
 * <td>GL_BLEND</td>
 * <td>disabled</td>
 * <td>EV | disabled</td>
 * </tr>
 * <tr>
 * <td>glBlendFunc</td>
 * <td>*</td>
 * <td>*</td>
 * </tr>
 * <tr>
 * <td>GL_TEXTURE_2D</td>
 * <td>*</td>
 * <td>disabled</td>
 * </tr>
 * </table>
 * </p> */
public class ShadedGroupStrategy implements GroupStrategy, Disposable {

	private static final int GROUP_OPAQUE = 0;
	private static final int GROUP_BLEND = 1;

	Pool<Array<Decal>> arrayPool = new Pool<Array<Decal>>(16) {
		@Override
		protected Array<Decal> newObject () {
			return new Array<Decal>();
		}
	};
	Array<Array<Decal>> usedArrays = new Array<Array<Decal>>();
	ObjectMap<DecalMaterial, Array<Decal>> materialGroups = new ObjectMap<DecalMaterial, Array<Decal>>();

	private Camera camera;
	private ShaderProgram shader;
	private final Comparator<Decal> cameraSorter;

	private boolean preventDepthTest = false;

	public ShadedGroupStrategy(final Camera camera) {
		this(camera, new Comparator<Decal>() {
			@Override
			public int compare (Decal o1, Decal o2) {
				float dist1 = camera.position.dst2(o1.getPosition());
				float dist2 = camera.position.dst2(o2.getPosition());
				return (int)Math.signum(dist2 - dist1);
			}
		});
	}

	public ShadedGroupStrategy(Camera camera, Comparator<Decal> sorter) {
		this.camera = camera;
		this.cameraSorter = sorter;
		createDefaultShader();

	}

	public void disableDepthTest(){
		preventDepthTest = true;
	}

	public void enableDepthTest(){
		preventDepthTest = false;
	}

	public void setCamera (Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera () {
		return camera;
	}

	@Override
	public int decideGroup (Decal decal) {
		return decal.getMaterial().isOpaque() ? GROUP_OPAQUE : GROUP_BLEND;
	}

	@Override
	public void beforeGroup (int group, Array<Decal> contents) {
		if (group == GROUP_BLEND) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			contents.sort(cameraSorter);
		} else {
			for (int i = 0, n = contents.size; i < n; i++) {
				Decal decal = contents.get(i);
				Array<Decal> materialGroup = materialGroups.get(decal.getMaterial());
				if (materialGroup == null) {
					materialGroup = arrayPool.obtain();
					materialGroup.clear();
					usedArrays.add(materialGroup);
					materialGroups.put(decal.getMaterial(), materialGroup);
				}
				materialGroup.add(decal);
			}

			contents.clear();
			for (Array<Decal> materialGroup : materialGroups.values()) {
				contents.addAll(materialGroup);
			}

			materialGroups.clear();
			arrayPool.freeAll(usedArrays);
			usedArrays.clear();
		}
	}

	@Override
	public void afterGroup (int group) {
		if (group == GROUP_BLEND) {
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}

	@Override
	public void beforeGroups() {
		if(!preventDepthTest)
			Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		shader.begin();
		shader.setUniformMatrix("u_projectionViewMatrix", camera.combined);
		shader.setUniformi("u_texture", 0);
	}

	@Override
	public void afterGroups () {
		shader.end();
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
	}

	private void createDefaultShader () {
		shader = new ShaderProgram(
				Gdx.files.internal("shaders/decal_vertex.glsl").readString(),
				Gdx.files.internal("shaders/decal_fragment.glsl").readString()
				);
		if (shader.isCompiled() == false) {
			throw new IllegalArgumentException("couldn't compile shader: " + shader.getLog());
		}
	}
	

	@Override
	public ShaderProgram getGroupShader (int group) {
		return shader;
	}
	

	@Override
	public void dispose () {
		if (shader != null) shader.dispose();
	}
}
