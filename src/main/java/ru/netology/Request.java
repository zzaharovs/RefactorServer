package ru.netology;

public class Request {

    private String status;
    private String path;
    private String versionHTTP;


    public Request(String status, String path, String versionHTTP) {
        this.status = status;
        this.path = path;
        this.versionHTTP = versionHTTP;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersionHTTP() {
        return versionHTTP;
    }

    public void setVersionHTTP(String versionHTTP) {
        this.versionHTTP = versionHTTP;
    }
}
