package com.chat.model;

import java.io.Serializable;

public class UserInf implements Serializable {
    private int account;
    private String username;
    private String password;
    private String tel;
    private String mail;
    public UserInf(){};
    public UserInf(int account,String username,String password,String mail,String tel)
    {
        this.account=account;
        this.username=username;
        this.password=password;
        this.mail=mail;
        this.tel=tel;
    }
    public int getAccount()
    {
        return account;
    }
    public void setAccount(int account)
    {
        this.account=account;
    }
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail=mail; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel=tel; }
}
