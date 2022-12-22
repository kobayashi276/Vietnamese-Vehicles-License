package com.example.recyclerview;

import java.util.ArrayList;

public class menu_class
{
    private String fullname;
    private int Pic;

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPic(int pic) {
        this.Pic = pic;
    }

    public int getPic() {
        return this.Pic;
    }

    public String getFullname() {
        return this.fullname;
    }

    public static ArrayList<menu_class> init(){
        String[] name = {"Lí thuyết","Biển báo","Câu hỏi liệt","Thi thử"};
        int[] pics = {R.drawable.gplx,R.drawable.bienbao,R.drawable.liet,R.drawable.gplx};
        menu_class p;
        ArrayList<menu_class> listMenu = new ArrayList<>();
        for (int i = 0;i< name.length;i++){
            p = new menu_class();
            p.setPic(pics[i]);
            p.setFullname(name[i]);
            listMenu.add(p);
        }
        return listMenu;
    }
}
