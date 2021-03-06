package edu.rpi.cs.yacs.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

import edu.rpi.cs.yacs.R;
import edu.rpi.cs.yacs.core.YACSApplication;
import edu.rpi.cs.yacs.enums.RecyclerViewMode;
import edu.rpi.cs.yacs.fragments.RecyclerViewFragment;
import edu.rpi.cs.yacs.models.Department;
import edu.rpi.cs.yacs.models.School;
import edu.rpi.cs.yacs.viewholders.SchoolItemViewHolder;

import java.util.List;

/**
 * Created by Mark Robinson on 10/15/16.
 */
public class SchoolsAdapter extends SimpleSectionedAdapter<SchoolItemViewHolder> {

    private List<School> schoolList = null;
    private RecyclerViewFragment recyclerViewFragment = null;

    public SchoolsAdapter(RecyclerViewFragment recyclerViewFragment, List<School> schoolList) {
        this.schoolList = schoolList;
        this.recyclerViewFragment = recyclerViewFragment;
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
        if (schoolList != null) {
            return schoolList.get(section).getName();
        }

        return "";
    }

    @Override
    protected int getSectionCount() {
        if (schoolList != null) {
            return schoolList.size();
        }

       return 0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (schoolList != null) {
            return schoolList.get(section).getDepartments().size();
        }

        return 0;
    }

    @Override
    protected SchoolItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.school_view_item, parent, false);

        return new SchoolItemViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(final SchoolItemViewHolder holder, final int section, final int position) {
        final School school = schoolList.get(section);
        final Department department = school.getDepartments().get(position);

        holder.render(department.getName(), department.getCode());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("Adapter", "Clicked");

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                      Expected behavior: animate to top, clear list, animate remove all items, load department courses

                        recyclerViewFragment.getMRecyclerView().smoothScrollToPosition(0);

                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                int count = getItemCount();

                                schoolList.clear();

                                notifyItemRangeRemoved(0, count);
                                recyclerViewFragment.getMAdapter().notifyItemRangeRemoved(0, count);
                            }
                        }, 600);

                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerViewFragment.populateCoursesAdapter(department.getCode());

                                YACSApplication.getInstance().setRecyclerViewMode(RecyclerViewMode.COURSES);
                            }
                        }, 1250);
                    }
                }, 500);
            }
        });
    }
}