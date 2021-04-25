package com.xuancanh.studentmanager.adapter;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, parent, false);;
        return new DataViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Student student = list.get(position);

        String s_no = student.getStu_no();
        String s_name = student.getStu_name();
        String s_class = student.getStu_class();
        String s_email = student.getStu_email();

        //Show image user if not null or show image male - female
        if(student.getStu_avt() != null) {
            Bitmap bmAvt = BitmapFactory.decodeByteArray(student.getStu_avt(), 0,student.getStu_avt().length);
            holder.ivStuAvt.setImageBitmap(bmAvt);
        }
        else {
            if(student.getStu_gender() == 1) {
                holder.ivStuAvt.setImageResource(R.drawable.male);
            }
            else if(student.getStu_gender() == 0) {
                holder.ivStuAvt.setImageResource(R.drawable.female);
            }
            else {
                holder.ivStuAvt.setImageResource(R.drawable.graduated);
            }
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
                    Toast.makeText(context, "Student: " + student.getStu_name(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(view.getContext(), UpdateActivity.class);
                    intent.putExtra("STUDENT_DATA", student);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

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
            //return false; // if not use
        }
    }
}
