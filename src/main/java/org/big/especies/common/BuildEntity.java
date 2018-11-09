package org.big.especies.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 *<p><b>构造实体类</b></p>
 *<p> 将传入的java对象对应至已有的Entity实体</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/12/04 21:58</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public class BuildEntity {

    @Autowired
    private static MessageSource messageSource;
    /**
     *<b>构造TeamEntity</b>
     *<p> 将传入的java对象对应至已有的TeamEntity实体</p>
     * @author WangTianshan (王天山)
     * @param obj 传入的Object对象
     * @return thisIdrecord（Idrecord）
     */
    public static JSONObject buildIdrecordJSON(Object obj){
        Object[] objs = (Object[]) obj;
        JSONObject idrecordJson =new JSONObject();
        idrecordJson.put("count",(long)objs[0]);
        idrecordJson.put("idrank",(String)objs[1]);
        idrecordJson.put("taxon",(String)objs[2]);
        idrecordJson.put("commonname",(String)objs[3]);
        return idrecordJson;
    }
    public static String buildString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[0];
    }
    public static String buildSpeciesString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[0]+" "+(String)objs[1];
    }
    public static String buildSpeciesNameString(Object obj){
        Object[] objs = (Object[]) obj;
        return (String)objs[0];
    }
    public static int buildGroupName(Object obj){
        Object[] objs = (Object[]) obj;
        //return (String)objs[1];
        return (int)objs[1];
    }
    public static Long buildCountGroup(Object obj){
        Object[] objs = (Object[]) obj;
        return (Long)objs[0];
    }
}
