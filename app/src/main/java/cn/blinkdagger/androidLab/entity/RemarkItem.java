package cn.blinkdagger.androidLab.entity;

/**
 * 类描述：
 * 创建人：ls
 * 创建时间：2018/1/24
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class RemarkItem {

    private String typeId;
    private String typeName;

    public RemarkItem(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
