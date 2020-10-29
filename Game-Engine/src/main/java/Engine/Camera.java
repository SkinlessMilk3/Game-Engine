package Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    private float aspectratio;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        aspectratio = (float)Window.getWidth()/(float)Window.getHeight();
        adjustProjection();

    }

    public void adjustProjection() {
        projectionMatrix.identity();
        //projectionMatrix.ortho(-aspectratio, aspectratio, -position.y/*0.0f*/, position.y/*1.0f/**Window.getHeight()*/, -1.0f, 1.0f);//-1.0f, 1.0f, -1.0f, 1.0f,-1.0f, 1.0f);//0.0f,/* Window.getWidth()32.0f * 40.0f*/, 0.0f,/* Window.getHeight()32.0f * 21.0f*/, 0.0f, 100.0f);
        //***POSSIBLY TEMPORARY Had to change back to original to get world corrdinates for draging and dropping object (Camera controller still seemingly works)***
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 1.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);

        this.viewMatrix.invert(inverseView);

        return this.viewMatrix;
    }

    public void setProjectionMatrix(float left, float right, float bottom, float top){
        projectionMatrix.identity().ortho(left, right, bottom, top, -1.0f, 1.0f);
    }
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return this.inverseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }
}