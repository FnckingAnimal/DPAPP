package app.dpapp.bean;

import java.util.List;

/**
 * Created by S7202916 on 2018/9/27.
 */
public class Resultvalue {
    private String Actionuri;
    private String Parenturi;
    private String Submituri;
    private String LinkActionuri;
    private String LinkComuri;
    private List<String> Othervalue;

    public List<String> getOthervalue() {
        return Othervalue;
    }

    public void setOthervalue(List<String> othervalue) {
        Othervalue = othervalue;
    }

    private SetName SetName;
    private BaseElementclass BaseElementclass;
    private List<String> LinkViewArray;

    public List<String> getLinkViewArray() {
        return LinkViewArray;
    }

    public void setLinkViewArray(List<String> linkViewArray) {
        LinkViewArray = linkViewArray;
    }

    private List<ElementViewArray> ElementViewArray;
    private List<ValueViewArray> ValueViewArray;
    private String Operationtype;
    private List<String> ComboxSource;

    public List<String> getComboxSource() {
        return ComboxSource;
    }

    public void setComboxSource(List<String> comboxSource) {
        ComboxSource = comboxSource;
    }

    public String getActionuri() {
        return Actionuri;
    }

    public void setActionuri(String actionuri) {
        Actionuri = actionuri;
    }

//    public Resultvalue.ComboxSource getComboxSource() {
//        return ComboxSource;
//    }
//
//    public void setComboxSource(Resultvalue.ComboxSource comboxSource) {
//        ComboxSource = comboxSource;
//    }

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

    public List<Resultvalue.ValueViewArray> getValueViewArray() {
        return ValueViewArray;
    }

    public void setValueViewArray(List<Resultvalue.ValueViewArray> valueViewArray) {
        ValueViewArray = valueViewArray;
    }

    public Resultvalue.SetName getSetName() {
        return SetName;
    }

    public void setSetName(Resultvalue.SetName setName) {
        SetName = setName;
    }

    public Resultvalue.BaseElementclass getBaseElementclass() {
        return BaseElementclass;
    }

    public void setBaseElementclass(Resultvalue.BaseElementclass baseElementclass) {
        BaseElementclass = baseElementclass;
    }


    public List<Resultvalue.ElementViewArray> getElementViewArray() {
        return ElementViewArray;
    }

    public void setElementViewArray(List<Resultvalue.ElementViewArray> elementViewArray) {
        ElementViewArray = elementViewArray;
    }

    public String getOperationtype() {
        return Operationtype;
    }

    public void setOperationtype(String operationtype) {
        Operationtype = operationtype;
    }
    static class Value {
        private String Item1;
        private String Item2;
        private String Item3;

        public String getItem1() {
            return Item1;
        }

        public void setItem1(String item1) {
            Item1 = item1;
        }

        public String getItem2() {
            return Item2;
        }

        public void setItem2(String item2) {
            Item2 = item2;
        }

        public String getItem3() {
            return Item3;
        }

        public void setItem3(String item3) {
            Item3 = item3;
        }
    }
    static class ElementViewArray {
        private String Key;
        private String Name;
        private String Discription;
        private RuntimeInfo RuntimeInfo;
        private Value Value;

        public Resultvalue.Value getValue() {
            return Value;
        }

        public void setValue(Resultvalue.Value value) {
            Value = value;
        }

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

        public Resultvalue.RuntimeInfo getRuntimeInfo() {
            return RuntimeInfo;
        }

        public void setRuntimeInfo(Resultvalue.RuntimeInfo runtimeInfo) {
            RuntimeInfo = runtimeInfo;
        }

    }
    static class SetName {
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

        public Resultvalue.RuntimeInfo getRuntimeInfo() {
            return RuntimeInfo;
        }

        public void setRuntimeInfo(Resultvalue.RuntimeInfo runtimeInfo) {
            RuntimeInfo = runtimeInfo;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }
    static class BaseElementclass {
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
    static class ValueViewArray {
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

        public Resultvalue.RuntimeInfo getRuntimeInfo() {
            return RuntimeInfo;
        }

        public void setRuntimeInfo(Resultvalue.RuntimeInfo runtimeInfo) {
            RuntimeInfo = runtimeInfo;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }

    static class RuntimeInfo {
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
}
