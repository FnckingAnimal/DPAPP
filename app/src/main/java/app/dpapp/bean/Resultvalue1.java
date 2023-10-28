package app.dpapp.bean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by S7187445 on 2018/9/28.
 */
public class Resultvalue1 {
    private String Actionuri;
    private String Parenturi;
    private String Submituri;
    private String LinkActionuri;
    private String LinkComuri;
    private List<String> Othervalue;
    private SetName SetName;
    private BaseElementclass BaseElementclass;
    private List<LinkViewArray> LinkViewArray;
    private List<String> ElementViewArray;
    private List<ValueViewArray> ValueViewArray;
    private String Operationtype;
    private List<String> ComboxSource;

    public List<String> getOthervalue() {
        return Othervalue;
    }

    public void setOthervalue(List<String> othervalue) {
        Othervalue = othervalue;
    }

    public List<String> getElementViewArray() {
        return ElementViewArray;
    }

    public void setElementViewArray(List<String> elementViewArray) {
        ElementViewArray = elementViewArray;
    }

    public List<String> getComboxSource() {
        return ComboxSource;
    }

    public void setComboxSource(LinkedList<String> comboxSource) {
        ComboxSource = comboxSource;
    }

    public String getActionuri() {
        return Actionuri;
    }

    public void setActionuri(String actionuri) {
        Actionuri = actionuri;
    }

    public String getParenturi() {
        return Parenturi;
    }

    public void setParenturi(String parenturi) {
        Parenturi = parenturi;
    }

    public String getSubmituri() {
        return Submituri;
    }

    public void setSubmituri(String submituri) {
        Submituri = submituri;
    }

    public String getLinkActionuri() {
        return LinkActionuri;
    }

    public void setLinkActionuri(String linkActionuri) {
        LinkActionuri = linkActionuri;
    }

    public String getLinkComuri() {
        return LinkComuri;
    }

    public void setLinkComuri(String linkComuri) {
        LinkComuri = linkComuri;
    }

    public Resultvalue1.SetName getSetName() {
        return SetName;
    }

    public void setSetName(Resultvalue1.SetName setName) {
        SetName = setName;
    }

    public Resultvalue1.BaseElementclass getBaseElementclass() {
        return BaseElementclass;
    }

    public void setBaseElementclass(Resultvalue1.BaseElementclass baseElementclass) {
        BaseElementclass = baseElementclass;
    }

    public List<Resultvalue1.LinkViewArray> getLinkViewArray() {
        return LinkViewArray;
    }

    public void setLinkViewArray(List<Resultvalue1.LinkViewArray> linkViewArray) {
        LinkViewArray = linkViewArray;
    }

    public List<Resultvalue1.ValueViewArray> getValueViewArray() {
        return ValueViewArray;
    }

    public void setValueViewArray(List<Resultvalue1.ValueViewArray> valueViewArray) {
        ValueViewArray = valueViewArray;
    }

    public String getOperationtype() {
        return Operationtype;
    }

    public void setOperationtype(String operationtype) {
        Operationtype = operationtype;
    }

    public static class LinkViewArray {
        private Item1 Item1;
        private Item2 Item2;
        private Item3 Item3;

        public Resultvalue1.Item1 getItem1() {
            return Item1;
        }

        public void setItem1(Resultvalue1.Item1 item1) {
            Item1 = item1;
        }

        public Resultvalue1.Item2 getItem2() {
            return Item2;
        }

        public void setItem2(Resultvalue1.Item2 item2) {
            Item2 = item2;
        }

        public Resultvalue1.Item3 getItem3() {
            return Item3;
        }

        public void setItem3(Resultvalue1.Item3 item3) {
            Item3 = item3;
        }
    }

    public static class Item1 {
        private List<String> HeaderRows;
        private List<List<Rows>> Rows;
        private List<Columns> Columns;
        private String Spanflag;
        private String Name;

        public List<String> getHeaderRows() {
            return HeaderRows;
        }

        public void setHeaderRows(List<String> headerRows) {
            HeaderRows = headerRows;
        }

        public List<List<Resultvalue1.Rows>> getRows() {
            return Rows;
        }

        public void setRows(List<List<Resultvalue1.Rows>> rows) {
            Rows = rows;
        }

        public List<Resultvalue1.Columns> getColumns() {
            return Columns;
        }

        public void setColumns(List<Resultvalue1.Columns> columns) {
            Columns = columns;
        }

        public String getSpanflag() {
            return Spanflag;
        }

        public void setSpanflag(String spanflag) {
            Spanflag = spanflag;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }

    public static class Columns {
           private String Name;
           private String Caption;
           private String Width;
           private String Suffix;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getCaption() {
            return Caption;
        }

        public void setCaption(String caption) {
            Caption = caption;
        }

        public String getWidth() {
            return Width;
        }

        public void setWidth(String width) {
            Width = width;
        }

        public String getSuffix() {
            return Suffix;
        }

        public void setSuffix(String suffix) {
            Suffix = suffix;
        }
    }
    public static class Rows{
        private List<String> CellLocation;
        private String Hightlightflag;
        private String Value;

        public List<String> getCellLocation() {
            return CellLocation;
        }

        public void setCellLocation(List<String> cellLocation) {
            CellLocation = cellLocation;
        }

        public String getHightlightflag() {
            return Hightlightflag;
        }

        public void setHightlightflag(String hightlightflag) {
            Hightlightflag = hightlightflag;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }
    public static class Item2 {
        private List<String> HeaderRows;
        private String Description;
        private String AssemblyName;
        private String NameSpaceName;
        private String TypeName;

        public List<String> getHeaderRows() {
            return HeaderRows;
        }

        public void setHeaderRows(List<String> headerRows) {
            HeaderRows = headerRows;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getAssemblyName() {
            return AssemblyName;
        }

        public void setAssemblyName(String assemblyName) {
            AssemblyName = assemblyName;
        }

        public String getNameSpaceName() {
            return NameSpaceName;
        }

        public void setNameSpaceName(String nameSpaceName) {
            NameSpaceName = nameSpaceName;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }
    }
    public static class Item3 {
        private String Description;
        private String AssemblyName;
        private String NameSpaceName;
        private String TypeName;

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getAssemblyName() {
            return AssemblyName;
        }

        public void setAssemblyName(String assemblyName) {
            AssemblyName = assemblyName;
        }

        public String getNameSpaceName() {
            return NameSpaceName;
        }

        public void setNameSpaceName(String nameSpaceName) {
            NameSpaceName = nameSpaceName;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }
    }


    public static class SetName {
        private String Key;
        private String Name;
        private String Discription;
        private RuntimeInfo RuntimeInfo;
        private String Value;

        public String getKey() {
            return Key;
        }

        public void setKey(String key) {
            Key = key;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDiscription() {
            return Discription;
        }

        public void setDiscription(String discription) {
            Discription = discription;
        }

        public Resultvalue1.RuntimeInfo getRuntimeInfo() {
            return RuntimeInfo;
        }

        public void setRuntimeInfo(Resultvalue1.RuntimeInfo runtimeInfo) {
            RuntimeInfo = runtimeInfo;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }

    public static class BaseElementclass {
        private String Description;
        private String AssemblyName;
        private String NameSpaceName;
        private String TypeName;

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getAssemblyName() {
            return AssemblyName;
        }

        public void setAssemblyName(String assemblyName) {
            AssemblyName = assemblyName;
        }

        public String getNameSpaceName() {
            return NameSpaceName;
        }

        public void setNameSpaceName(String nameSpaceName) {
            NameSpaceName = nameSpaceName;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }
    }

    public static class RuntimeInfo {
        private String Description;
        private String AssemblyName;
        private String NameSpaceName;
        private String TypeName;

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getAssemblyName() {
            return AssemblyName;
        }

        public void setAssemblyName(String assemblyName) {
            AssemblyName = assemblyName;
        }

        public String getNameSpaceName() {
            return NameSpaceName;
        }

        public void setNameSpaceName(String nameSpaceName) {
            NameSpaceName = nameSpaceName;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }
    }

    public static class ValueViewArray {
        private String Key;
        private String Name;
        private String Discription;
        private RuntimeInfo RuntimeInfo;
        private String Value;

        public String getKey() {
            return Key;
        }

        public void setKey(String key) {
            Key = key;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDiscription() {
            return Discription;
        }

        public void setDiscription(String discription) {
            Discription = discription;
        }

        public Resultvalue1.RuntimeInfo getRuntimeInfo() {
            return RuntimeInfo;
        }

        public void setRuntimeInfo(Resultvalue1.RuntimeInfo runtimeInfo) {
            RuntimeInfo = runtimeInfo;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }
}
