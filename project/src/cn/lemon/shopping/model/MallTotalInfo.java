package cn.lemon.shopping.model;

import java.util.List;
import android.util.SparseArray;

public class MallTotalInfo {

    public String mVersion;

    public List<CategoryEntryInfo> mCategoryList;

    public SparseArray<List<MallEntryInfo>> mCategoryMappingMall;

}
