package com.vektor.sourfer.ui;

import java.util.ArrayList;
import java.util.TreeMap;

import com.vektor.model.VektorSerialization.classDocument;
import com.vektor.model.VektorSerialization.classField;
import com.vektor.model.VektorSerialization.classMethod;
import com.vektor.model.VektorSerialization.classStructure;
import com.vektor.sourfer.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import pl.polidea.treeview.AbstractTreeViewAdapter;
import pl.polidea.treeview.TreeBuilder;
import pl.polidea.treeview.TreeNodeInfo;
import pl.polidea.treeview.TreeStateManager;

public class ClassStructAdapter extends AbstractTreeViewAdapter<Long> {
	private classDocument doc;
	private TreeBuilder<Long> treeBuilder;
	private Context context;
	private TreeMap<Long, classStructure> types;
	private TreeMap<Long, classField> fields;
	private TreeMap<Long, classMethod> methods;
	private int i=0;
	private ListView associatedList;
	public ClassStructAdapter(Activity activity,
			TreeStateManager<Long> manager, int numberOfLevels,
			classDocument doc, ListView list) {
		super(activity, manager, numberOfLevels);
		this.context = activity.getApplicationContext();
		associatedList = list;
		types = new TreeMap<Long, classStructure>();
		fields = new TreeMap<Long, classField>();
		methods = new TreeMap<Long, classMethod>();
		
		// this.doc=doc;
		treeBuilder = new TreeBuilder<Long>(manager);
		Log.i("doc info", doc.getNestlevel() + " nesting levels.");
		buildTree( 0, doc);
	}

	private void buildTree( int level, classDocument doc) {
		for (classStructure struct : doc.getClasses()) {
			treeBuilder.sequentiallyAddNextNode((long) i, level);
			types.put((long) i, struct);
			i++;
			buildTree( level + 1, struct.getTypes());
			for (classField field : struct.getFields()) {
				treeBuilder.sequentiallyAddNextNode((long) i, level+1);
				fields.put((long) i, field);
				i++;
			}
			for (classMethod method : struct.getMethods()) {
				treeBuilder.sequentiallyAddNextNode((long) i, level+1);
				methods.put((long) i, method);
				i++;
			}
		}

	}

	private void buildTree( int level, ArrayList<classStructure> structs) {
		for (classStructure struct : structs) {
			treeBuilder.sequentiallyAddNextNode((long) i, level);
			types.put((long) i, struct);
			i++;
			buildTree(level + 1, struct.getTypes());
			for (classField field : struct.getFields()) {
				treeBuilder.sequentiallyAddNextNode((long) i, level+1);
				fields.put((long) i, field);
				i++;
			}
			for (classMethod method : struct.getMethods()) {
				treeBuilder.sequentiallyAddNextNode((long) i, level+1);
				methods.put((long) i, method);
				i++;
			}
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getNewChildView(TreeNodeInfo<Long> treeNodeInfo) {

		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.class_struct_row, null);
		return updateView(layout, treeNodeInfo);
	}

	@Override
	public View updateView(View view, TreeNodeInfo<Long> treeNodeInfo) {
		long id = treeNodeInfo.getId();
		if (id % 2 == 0)
			view.setBackgroundColor(Color.parseColor(Theme.getRow1()));
		else
			view.setBackgroundColor(Color.parseColor(Theme.getRow2()));
		TextView name = (TextView) view.findViewById(R.id.element_name);
		ImageView type = (ImageView) view.findViewById(R.id.element_type);
		TextView spacer = (TextView) view.findViewById(R.id.spacer);
		TextView elemtype = (TextView) view.findViewById(R.id.elem_type);
		
		// Log.i("Nodes","ID="+treeNodeInfo.getId()+",LVL="+treeNodeInfo.getLevel()+",");
		if (fields.containsKey(id)) {
			classField field = fields.get(id);
			name.setText(field.getName());
			elemtype.setText(field.getType());
			if(field.getAccess().equals("public")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.public_field));
			}
			else if(field.getAccess().equals("protected")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.protected_field));
			}
			else if(field.getAccess().equals("private")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.private_field));
			}
			else {
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.field));
			}
			
		}
		if (methods.containsKey(id)) {
			classMethod method = methods.get(id);
			name.setText(method.getName());
			elemtype.setText(method.getReturnType());
			if(method.getAccess().equals("public")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.public_method));
			}
			else if(method.getAccess().equals("protected")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.protected_method));
			}
			else if(method.getAccess().equals("private")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.private_method));
			}
			else {
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.method));
			}
			
		}
		if (types.containsKey(id)) {
			classStructure classe = types.get(id);
			name.setText(classe.getName());
			spacer.setVisibility(View.GONE);
			elemtype.setVisibility(View.GONE);
			if(classe.getAccess().equals("public")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.public_class));
			}
			else if(classe.getAccess().equals("protected")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.protected_class));
			}
			else if(classe.getAccess().equals("private")){
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.private_class));
			}
			else {
				type.setImageDrawable(context.getResources().getDrawable(R.drawable.classe));
			}
		}

		return view;
	}

	@Override
	public Drawable getBackgroundDrawable(final TreeNodeInfo<Long> treeNodeInfo) {
		long l = treeNodeInfo.getId() % 2;
		if (l == 0) {
			return new ColorDrawable(Color.parseColor(Theme.getRow1()));
		} else {
			return new ColorDrawable(Color.parseColor(Theme.getRow2()));
		}
	}
	
	@Override
    public void handleItemClick(final View view, final Object id) {
        final Long longId = (Long) id;
        final TreeNodeInfo<Long> info = getManager().getNodeInfo(longId);
        if (info.isWithChildren()) {
            super.handleItemClick(view, id);
        } else {
            if(methods.containsKey((Long)id)){
            	classMethod method = methods.get((Long)id);
            	SourceRowAdapter sra = (SourceRowAdapter)associatedList.getAdapter();
            	sra.setHighlight(method.getLineStart()-1, method.getLineEnd()-1);
            	sra.notifyDataSetChanged();
            	associatedList.setSelection(method.getLineStart()-1);
            }
            else if(fields.containsKey((Long)id)){
            	classField field = fields.get((Long)id);
            	SourceRowAdapter sra = (SourceRowAdapter)associatedList.getAdapter();
            	sra.setHighlight(field.getLineStart()-1, field.getLineEnd()-1);
            	sra.notifyDataSetChanged();
            	associatedList.setSelection(field.getLineStart()-1);
            }
        }
    }

	
}