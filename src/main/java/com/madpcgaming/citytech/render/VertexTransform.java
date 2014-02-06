package com.madpcgaming.citytech.render;

import com.madpcgaming.citytech.vecmath.Vector3d;
import com.madpcgaming.citytech.vecmath.Vector3f;

public interface VertexTransform
{
	public abstract void apply(Vector3d vec);

	public abstract void applyToNormal(Vector3f vec);
}
