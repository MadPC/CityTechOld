package com.madpcgaming.citytech.vecmath;

import java.awt.Rectangle;

public class VecmathUtil
{

	public static double distanceFromPointToPlane(Vector4d plane, Vector3d point)
	{
		Vector4d newPoint = new Vector4d(point.x, point.y, point.z, 1);
		return plane.dot(newPoint);
	}

	public static void computePlaneEquation(Vector4d a, Vector4d b, Vector4d c,
			Vector4d result)
	{
		computePlaneEquation(new Vector3d(a.x, a.y, a.z), new Vector3d(b.x,
				b.y, b.z), new Vector3d(c.x, c.y, c.z), result);
	}

	public static Vector3d clamp(Vector3d v, double min, double max)
	{
		v.x = clamp(v.x, min, max);
		v.y = clamp(v.y, min, max);
		v.z = clamp(v.z, min, max);
		return v;
	}

	public static double clamp(double val, double min, double max)
	{
		return val < min ? min : (val > max ? max : val);
	}

	public static int clamp(int val, int min, int max)
	{
		return val < min ? min : (val > max ? max : val);
	}

	public static void computePlaneEquation(Vector3d a, Vector3d b, Vector3d c,
			Vector4d result)
	{
		Vector3d i = new Vector3d();
		Vector3d j = new Vector3d();
		Vector3d k = new Vector3d();

		// compute normal vector
		i.x = c.x - a.x;
		i.y = c.y - a.y;
		i.z = c.z - a.z;

		j.x = b.x - a.x;
		j.y = b.y - a.y;
		j.z = b.z - a.z;

		k.cross(j, i);
		k.normalize();

		// plane equation: Ax + By + Cz + D = 0
		result.x = k.x; // A
		result.y = k.y; // B
		result.z = k.z; // C
		result.w = -(result.x * a.x + result.y * a.y + result.z * a.z); // D
	}

	public static void projectPointOntoPlane(Vector4d plane, Vector4d point)
	{
		double distance = plane.dot(point);
		Vector4d newPoint = new Vector4d(point);
		Vector3d planeNormal = new Vector3d(plane.x, plane.y, plane.z);
		planeNormal.normalize();
		planeNormal.scale(distance);
		newPoint.sub(new Vector4d(planeNormal.x, planeNormal.y, planeNormal.z,
				0));
		point.set(newPoint);
	}

	public static Vector3d computeIntersectionBetweenPlaneAndLine(
			Vector4d plane, Vector3d pointInLine, Vector3d lineDirection)
	{
		// check for no intersection
		Vector3d planeNormal = new Vector3d(plane.x, plane.y, plane.z);
		if (planeNormal.dot(lineDirection) == 0) {
			// line and plane are perpendicular
			return null;
		}
		// check if line is on the plane
		if (planeNormal.dot(pointInLine) + plane.w == 0) {
			return null;
		}

		// we have an intersection
		Vector4d point = new Vector4d(pointInLine.x, pointInLine.y, pointInLine.z, 1);
		Vector4d lineNorm = new Vector4d(lineDirection.x, lineDirection.y, lineDirection.z, 0);
		double t = -(plane.dot(point) / plane.dot(lineNorm));

		Vector3d result = new Vector3d(pointInLine);
		lineDirection.scale(t);
		result.add(lineDirection);
		return result;
	}

	public static void computeRayForPixel(Rectangle vp, Matrix4d ipm,
			Matrix4d ivm, int x, int y, Vector3d eyeOut, Vector3d normalOut)
	{

		// grab the eye's position
		ivm.getTranslation(eyeOut);

		Matrix4d vpm = new Matrix4d();
		vpm.mul(ivm, ipm);

		// Calculate the pixel location in screen clip space (width and height
		// from
		// -1 to 1)
		double screenX = (x - vp.getX()) / vp.getWidth();
		double screenY = (y - vp.getY()) / vp.getHeight();
		screenX = (screenX * 2.0) - 1.0;
		screenY = (screenY * 2.0) - 1.0;

		// Now calculate the XYZ location of this point on the near plane
		Vector4d tmp = new Vector4d();
		tmp.x = screenX;
		tmp.y = screenY;
		tmp.z = -1;
		tmp.w = 1.0;
		vpm.transform(tmp);

		double w = tmp.w;
		Vector3d nearXYZ = new Vector3d(tmp.x / w, tmp.y / w, tmp.z / w);

		// and then on the far plane
		tmp.x = screenX;
		tmp.y = screenY;
		tmp.z = 1;
		tmp.w = 1.0;
		vpm.transform(tmp);

		w = tmp.w;
		Vector3d farXYZ = new Vector3d(tmp.x / w, tmp.y / w, tmp.z / w);

		normalOut.set(farXYZ);
		normalOut.sub(nearXYZ);
		normalOut.normalize();

	}

	public static Matrix4d createProjectionMatrixAsPerspective(
			double fovDegrees, double near, double far, int viewportWidth,
			int viewportHeight)
	{

		Matrix4d matrix = new Matrix4d();
		// for impl details see gluPerspective doco in OpenGL reference manual
		double aspect = (double) viewportWidth / (double) viewportHeight;

		double theta = (Math.toRadians(fovDegrees) / 2d);
		double f = Math.cos(theta) / Math.sin(theta);

		double a = (far + near) / (near - far);
		double b = (2d * far * near) / (near - far);

		matrix.set(new double[] { f / aspect, 0, 0, 0, 0, f, 0, 0, 0, 0, a, b,	0, 0, -1, 0 });

		return matrix;
	}

	public static Matrix4d createProjectionMatrixAsPerspective(double left,
			double right, double bottom, double top, double zNear, double zFar)
	{

		double A = (right + left) / (right - left);
		double B = (top + bottom) / (top - bottom);
		double C = (Math.abs(zFar) > Double.MAX_VALUE) ? -1. : -(zFar + zNear) / (zFar - zNear);
		double D = (Math.abs(zFar) > Double.MAX_VALUE) ? -2. * zNear : -2.0 * zFar * zNear / (zFar - zNear);

		Matrix4d matrix = new Matrix4d();
		matrix.set(new double[] { 2.0 * zNear / (right - left), 0.0, 0.0, 0.0,	0.0, 2.0 * zNear / (top - bottom), 0.0, 0.0, A, B, C, -1.0,	0.0, 0.0, D, 0.0});

		matrix.transpose();
		return matrix;
	}

	public static Matrix4d createProjectionMatrixAsOrtho(double left,
			double right, double bottom, double top, double near, double far)
	{

		Matrix4d matrix = new Matrix4d();
		// for impl details see glOrtho doco in OpenGL reference manual
		double tx = -((right + left) / (right - left));
		double ty = -((top + bottom) / (top - bottom));
		double tz = -((far + near) / (far - near));

		matrix.set(new double[] { 2d / (right - left), 0, 0, tx, 0,	2d / (top - bottom), 0, ty, 0, 0, -2d / (far - near), tz, 0, 0,	0, 1 });

		return matrix;
	}

	public static void setNearFarOnPerspectiveProjectionMatrix(
			Matrix4d projMat, double near, double far)
	{

		projMat.transpose();

		double transNearPlane = (-near * projMat.getElement(2, 2) + projMat.getElement(3, 2))/ (-near * projMat.getElement(2, 3) + projMat.getElement(3, 3));
		double transFarPlane = (-far * projMat.getElement(2, 2) + projMat.getElement(3, 2))/ (-far * projMat.getElement(2, 3) + projMat.getElement(3, 3));

		double ratio = Math.abs(2.0 / (transNearPlane - transFarPlane));
		double center = -(transNearPlane + transFarPlane) / 2.0;

		Matrix4d mat = new Matrix4d();
		mat.setIdentity();
		mat.setElement(2, 2, ratio);
		mat.setElement(3, 2, center * ratio);

		projMat.mul(mat);
		projMat.transpose();

	}

	public static Matrix4d createMatrixAsLookAt(Vector3d eyePos,
			Vector3d lookAtPos, Vector3d upVec)
	{

		Vector3d eye = new Vector3d(eyePos);
		Vector3d lookAt = new Vector3d(lookAtPos);
		Vector3d up = new Vector3d(upVec);

		Vector3d forwardVec = new Vector3d(lookAt);
		forwardVec.sub(eye);
		forwardVec.normalize();

		Vector3d sideVec = new Vector3d();
		sideVec.cross(forwardVec, up);
		sideVec.normalize();

		Vector3d upVed = new Vector3d();
		upVed.cross(sideVec, forwardVec);
		upVed.normalize();

		Matrix4d mat = new Matrix4d(sideVec.x, sideVec.y, sideVec.z, 0,	upVed.x, upVed.y, upVed.z, 0, -forwardVec.x, -forwardVec.y, -forwardVec.z, 0, 0, 0, 0, 1);

		eye.negate();
		// mat.transform(eye);
		mat.transformNormal(eye);
		mat.setTranslation(eye);

		return mat;
	}

	public static Vector3d preMultiply(Vector3d v, Matrix4d mat)
	{
		Matrix4d m = new Matrix4d(mat);
		m.transpose();
		double d = 1.0f / (m.getElement(0, 3) * v.x + m.getElement(1, 3) * v.y 	+ m.getElement(2, 3) * v.z + m.getElement(3, 3));
		double x = (m.getElement(0, 0) * v.x + m.getElement(1, 0) * v.y + m.getElement(2, 0) * v.z + m.getElement(3, 0))* d;
		double y = (m.getElement(0, 1) * v.x + m.getElement(1, 1) * v.y + m.getElement(2, 1) * v.z + m.getElement(3, 1)) * d;
		double z = (m.getElement(0, 2) * v.x + m.getElement(1, 2) * v.y 	+ m.getElement(2, 2) * v.z + m.getElement(3, 2) * d);
		return new Vector3d(x, y, z);
	}


	public static Vector4d[] getEyePlanesForMatrix(Matrix4d matrix)
	{

		Matrix4d copy = new Matrix4d(matrix);
		copy.transpose();

		Vector4d[] res = new Vector4d[4];
		// s plane
		res[0] = new Vector4d(copy.getElement(0, 0), copy.getElement(1, 0),	copy.getElement(2, 0), copy.getElement(3, 0));
		// t plane
		res[1] = new Vector4d(copy.getElement(0, 1), copy.getElement(1, 1),	copy.getElement(2, 1), copy.getElement(3, 1));
		// r plane
		res[2] = new Vector4d(copy.getElement(0, 2), copy.getElement(1, 2),	copy.getElement(2, 2), copy.getElement(3, 2));
		// q plane
		res[3] = new Vector4d(copy.getElement(0, 3), copy.getElement(1, 3),	copy.getElement(2, 3), copy.getElement(3, 3));

		return res;
	}

	public static Vector3d cross(Vector3d vec1, Vector3d vec2)
	{
		Vector3d res = new Vector3d();
		res.cross(new Vector3d(vec1), new Vector3d(vec2));
		return res;
	}


	public static double distance(Vector3d from, Vector3d to)
	{
		return from.distance(to);
	}

	public static double distanceSquared(Vector3d from, Vector3d to)
	{
		return from.distanceSquared(to);
	}

	public static void getVectorsForMatrix(Matrix4d matrix, Vector3d upVecOut, Vector3d sideVecOut, Vector3d lookVecOut)
	{

		sideVecOut.set(matrix.getElement(0, 0), matrix.getElement(0, 1), matrix.getElement(0, 2));
		sideVecOut.normalize();
		upVecOut.set(matrix.getElement(1, 0), matrix.getElement(1, 1), matrix.getElement(1, 2));
		upVecOut.normalize();
		lookVecOut.set(matrix.getElement(2, 0), matrix.getElement(2, 1), matrix.getElement(2, 2));
		lookVecOut.negate();
		lookVecOut.normalize();

	}

	public static Vector3d getUpFromMatrix(Matrix4d matrix)
	{
		Vector3d res = new Vector3d(matrix.getElement(1, 0), matrix.getElement(1, 1), matrix.getElement(1, 2));
		res.normalize();
		return res;
	}

}