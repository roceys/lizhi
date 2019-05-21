package ren.perry.lizhi.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author perrywe
 * @date 2019-05-16
 * WeChat  917351143
 */
@Entity
public class Music {

    @Id
    private Long id;

    @Property(nameInDb = "m_id")
    private int m_id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "url")
    private String url;

    @Property(nameInDb = "pic")
    private String pic;

    @Property(nameInDb = "album")
    private String album;

    @Property(nameInDb = "album_pic")
    private String album_pic;

    @Property(nameInDb = "singer")
    private String singer;

    @Property(nameInDb = "create_time")
    private String create_time;

    @Property(nameInDb = "add_time")
    private long add_time;

    @Generated(hash = 481097759)
    public Music(Long id, int m_id, String name, String url, String pic, String album,
            String album_pic, String singer, String create_time, long add_time) {
        this.id = id;
        this.m_id = m_id;
        this.name = name;
        this.url = url;
        this.pic = pic;
        this.album = album;
        this.album_pic = album_pic;
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

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum_pic() {
        return this.album_pic;
    }

    public void setAlbum_pic(String album_pic) {
        this.album_pic = album_pic;
    }

    public int getM_id() {
        return this.m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }
}
