package com.xuancanh.studentmanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuancanh.studentmanager.ItemClickListener;
import com.xuancanh.studentmanager.R;
import com.xuancanh.studentmanager.UpdateActivity;
import com.xuancanh.studentmanager.ViewAllActivity;
import com.xuancanh.studentmanager.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.DataViewHolder> {

    //Form for adapter
    Context context;
    ArrayList<Student> list;

    public StudentAdapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        //Check item view type, if 1 -> inflate layout list_item_student_male.xml, 2 -> inflate layout list_item_student_female.xml
        switch (viewType) {
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student_male, parent, false);
                break;
            case 2:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student_female, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student_male, parent, false);
                break;
        }
        return new DataViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        //Set item view type for every line, if male return 1, female return 2.
        if(list.get(position).getStu_gender() == 1){
            return 1;
        }
        else if(list.get(position).getStu_gender() == 0){
            return 2;
        }
        else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Student student = list.get(position);

        String s_no = student.getStu_no();
        String s_name = student.getStu_name();
        String s_class = student.getStu_class();
        String s_email = student.getStu_email();

        if(student.getStu_avt() != null) {
            Bitmap bmAvt = BitmapFactory.decodeByteArray(student.getStu_avt(), 0,student.getStu_avt().length);
            holder.ivStuAvt.setImageBitmap(bmAvt);
        }


        holder.tvStuNo.setText(s_no);
        holder.tvStuName.setText(s_name);
        holder.tvStuClass.setText(s_class);
        holder.tvStuEmail.setText(s_email);


        //Click for RecycleView

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick) {
                    Toast.makeText(context, "Long Click: " + student.getStu_name(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(view.getContext(), UpdateActivity.class);
                    intent.putExtra("STUDENT_DATA", student);
                    view.getContext().startActivity(intent);

                    //Toast.makeText(context, student.getStu_name(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }





//
//    @Override
//    public int getCount() {
//        //Return num of lines that adapter draws(Student)
//        //If just want to draw x line -> return x(so much lines need it)
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        DataViewHolder dataViewHolder;
//        if (convertView==null){
//
//            dataViewHolder = new DataViewHolder();
//
//            //Convert resoure(list_item_student) to View
//
//
//
//            convertView.setTag(dataViewHolder);
//        }
//        else {
//            dataViewHolder = (DataViewHolder) convertView.getTag();
//        }
//        return null;
//    }


    //Data ViewHolder class
    public static class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        ImageView ivStuAvt;
        TextView tvStuNo, tvStuName, tvStuClass, tvStuEmail;
        Button btnEdit, btnDelete;

        ItemClickListener itemClickListener;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStuNo = (TextView) itemView.findViewById(R.id.tv_stu_no);
            tvStuName = (TextView) itemView.findViewById(R.id.tv_stu_name);
            tvStuClass = (TextView) itemView.findViewById(R.id.tv_stu_class);
            tvStuEmail = (TextView) itemView.findViewById(R.id.tv_stu_email);

            ivStuAvt = (ImageView)itemView.findViewById(R.id.iv_stu_avt);

            btnDelete = (Button)itemView.findViewById(R.id.btn_stu_delete);
            btnEdit = (Button)itemView.findViewById(R.id.btn_stu_edit);


            //Turn On Click for RecycleView
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        //onClick for RecycleView
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }


        //onLongClick for RecycleView
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
            //return false;
        }
    }



}
