package org.big.especies.repository;

import org.big.especies.entity.User;
import org.big.especies.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *<p><b>User的DAO类接口</b></p>
 *<p> User的DAO类接口，与User有关的持久化操作方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/10/31 15:30</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface UserRepository extends BaseRepository<User, String> {

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    @Query(value = "select u from User u" +
            " where (" +
            "u.username like %?1% " +
            "or u.email like %?1% " +
            "or u.nickname like %?1% " +
            "or u.mobile like %?1%)"
    )
    Page<User> searchInfo(
            String findText,
            Pageable pageable
    );

    /**
     *<b>根据User的Username查询一个符合条件的User</b>
     *<p> 根据User的Username查询一个符合条件的User</p>
     * @author WangTianshan (王天山)
     * @param name 团队name
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    User findOneByUsername(String name);


    /**
     *<b>根据User的Email查询一个符合条件的User</b>
     *<p> 根据User的Email查询一个符合条件的User</p>
     * @author WangTianshan (王天山)
     * @param name 团队name
     * @return org.springframework.data.domain.Page<org.big.entity.User>
     */
    User findOneByEmail(String name);
}
