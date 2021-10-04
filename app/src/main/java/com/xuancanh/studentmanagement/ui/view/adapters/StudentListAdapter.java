package com.xuancanh.studentmanagement.ui.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuancanh.studentmanagement.presentation.model.StudentDTO;
import com.xuancanh.studentmanagement.ui.activities.UpdateActivity;
import com.xuancanh.studentmanagement.ui.interfaces.ItemClickListener;
import com.xuancanh.studentmanager.R;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    //Form for adapter
    Context context;
    List<StudentDTO> list;

    public StudentListAdapter(Context context, List<StudentDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, parent, false);
        return new StudentViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentDTO student = list.get(position);

        String s_name = student.getStu_name();
        String s_class = student.getStu_class();

        //Show image user if not null or show image male - female
        if (student.getStu_avt() != null) {
            Bitmap bmAvt = BitmapFactory.decodeByteArray(student.getStu_avt(), 0, student.getStu_avt().length);
            holder.ivStuAvt.setImageBitmap(bmAvt);
        } else {
            if (student.getStu_gender() == 1) {
                holder.ivStuAvt.setImageResource(R.drawable.male);
            } else if (student.getStu_gender() == 0) {
                holder.ivStuAvt.setImageResource(R.drawable.female);
            } else {
                holder.ivStuAvt.setImageResource(R.drawable.graduated);
            }
        }

        holder.tvStuName.setText(s_name);
        holder.tvStuClass.setText(s_class);

        //Click for RecycleView
        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick) {
                Toast.makeText(context, "StudentDTO: " + student.getStu_name(), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(view.getContext(), UpdateActivity.class);
                intent.putExtra("STUDENT_DATA", student);
                view.getContext().startActivity(intent);
                ((Activity)view.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    //Data ViewHolder class
    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivStuAvt;
        TextView tvStuName, tvStuClass;

        ItemClickListener itemClickListener;



        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStuName = (TextView) itemView.findViewById(R.id.tv_stu_name);
            tvStuClass = (TextView) itemView.findViewById(R.id.tv_stu_class);
            ivStuAvt = (ImageView) itemView.findViewById(R.id.iv_stu_avt);

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
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
            //return false; // if not use
        }
    }
}
