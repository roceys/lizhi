package ren.perry.lizhi.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
@Entity
public class Music {

    @Id
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "url")
    private String url;

    @Property(nameInDb = "pic")
    private String pic;

    @Property(nameInDb = "singer")
    private String singer;

    @Property(nameInDb = "create_time")
    private String create_time;

    @Property(nameInDb = "add_time")
    private long add_time;

    @Generated(hash = 1446613876)
    public Music(Long id, String name, String url, String pic, String singer,
            String create_time, long add_time) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.pic = pic;
        this.singer = singer;
        this.create_time = create_time;
        this.add_time = add_time;
    }

    @Generated(hash = 1263212761)
    public Music() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSinger() {
        return this.singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public long getAdd_time() {
        return this.add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }
}
