package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName data_source
 */
@TableName(value ="data_source")
public class DataSource implements Serializable {
    /**
     * 主键，数据源ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据表（文件名）- 类型
     */
    private String dataAndType;

    /**
     * 数据源类型
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键，数据源ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键，数据源ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 桶名称
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 桶名称
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 数据库名称
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * 数据库名称
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * 数据表（文件名）- 类型
     */
    public String getDataAndType() {
        return dataAndType;
    }

    /**
     * 数据表（文件名）- 类型
     */
    public void setDataAndType(String dataAndType) {
        this.dataAndType = dataAndType;
    }

    /**
     * 数据源类型
     */
    public String getType() {
        return type;
    }

    /**
     * 数据源类型
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        DataSource other = (DataSource) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getBucketName() == null ? other.getBucketName() == null : this.getBucketName().equals(other.getBucketName()))
            && (this.getDbName() == null ? other.getDbName() == null : this.getDbName().equals(other.getDbName()))
            && (this.getDataAndType() == null ? other.getDataAndType() == null : this.getDataAndType().equals(other.getDataAndType()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getBucketName() == null) ? 0 : getBucketName().hashCode());
        result = prime * result + ((getDbName() == null) ? 0 : getDbName().hashCode());
        result = prime * result + ((getDataAndType() == null) ? 0 : getDataAndType().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", url=").append(url);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", bucketName=").append(bucketName);
        sb.append(", dbName=").append(dbName);
        sb.append(", dataAndType=").append(dataAndType);
        sb.append(", type=").append(type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}