package com.madpcgaming.citytech.render;

public interface VertexTransform
{
	public abstract void apply(Vector3d vec);

	public abstract void applyToNormal(Vector3f vec);
}
