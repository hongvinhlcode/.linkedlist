package hongvinhdemo.demo.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ToDoAdapter extends BaseAdapter {

    private MainActivity context;
    private int layout;
    private List<ToDo> toDoList;

    public ToDoAdapter(MainActivity context, int layout, List<ToDo> toDoList) {
        this.context = context;
        this.layout = layout;
        this.toDoList = toDoList;
    }

    @Override
    public int getCount() {
        return toDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtContent;
        ImageButton btnDel,btnEdit;
        ImageView imgArrow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);

            holder.txtContent=(TextView) convertView.findViewById(R.id.txtContent);
            holder.btnDel=(ImageButton) convertView.findViewById(R.id.btnDel);
            holder.btnEdit=(ImageButton) convertView.findViewById(R.id.btnEdit);
            holder.imgArrow=(ImageView) convertView.findViewById(R.id.imgArrow);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        final ToDo toDo=toDoList.get(position);

        holder.txtContent.setText(toDo.getName());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.ShowDialogEdit(toDo.getName(),toDo.getId());
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.ShowDialogDel(toDo.getId());
            }
        });
        return convertView;
    }
}
