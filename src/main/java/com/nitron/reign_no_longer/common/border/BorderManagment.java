package com.nitron.reign_no_longer.common.border;

public class BorderManagment {
    public double minX, minY, minZ, maxX, maxY, maxZ;
    public boolean isOn;

    public BorderManagment(double centerX, double centerY, double centerZ, double size){
        updateBorder(centerX, centerY, centerZ, size);
    }

    public void updateBorder(double centerX, double centerY, double centerZ, double size){
        double halfSize = size / 2.0;
        this.minX = centerX - halfSize;
        this.maxX = centerX + halfSize;
        this.minY = centerY - halfSize;
        this.maxY = centerY + halfSize;
        this.minZ = centerZ - halfSize;
        this.maxZ = centerZ + halfSize;
    }

    public boolean isOutside(double x, double y, double z){
        return x < minX + 0.5 || x > maxX - 0.5 || y < minY || y > maxY - 2 || z < minZ + 0.5 || z > maxZ - 0.5;
    }

    public boolean isInside(double x, double y, double z){
        return x > minX + 0.5 || x < maxX - 0.5 || y > minY || y < maxY - 2 || z > minZ + 0.5 || z < maxZ - 0.5;
    }

    public boolean isTooFarOutside(double x, double y, double z){
        return x < minX - 1 || x > maxX + 1 || y < minY - 1 || y > maxY - 1 || z < minZ - 1 || z > maxZ + 1;
    }

    public void setOn(boolean value){
        this.isOn = value;
    }
}
