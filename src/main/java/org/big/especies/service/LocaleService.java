package org.big.especies.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *<p><b>Locale的Service类接口</b></p>
 *<p> Locale的Service类接口，与Locale有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/11/9 15:31</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface LocaleService {

    /**
     *<b>返回当前的语言</b>
     *<p> 据request、response返回当前的语言</p>
     * @author WangTianshan (王天山)
     * @param request 页面请求
     * @param response 页面响应
     * @return java.lang.String
     */
    String getLanguage(HttpServletRequest request, HttpServletResponse response);
}