package org.ats.entities;

import java.util.Comparator;

public class DepartmentTitleCompare implements Comparator<Department> {

    @Override
    public int compare(Department o1, Department o2) {
        return o1.getDepartmentName().compareTo(o2.getDepartmentName());
    }
}
