package web.sontan.dao;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import web.sontan.model.Find;
import web.sontan.model.Order;

import java.util.List;

/**
 * Created by 臻少 on 2019/5/17.
 */

@Repository
@Mapper
public interface FindDao {
    //------------------物品模块--------------------//
    /**
     * 查找所有物品（index首页，三种类型齐上阵）
     * @return 返回所有物品信息
     */
    List<Find> findAllFinds();                               //查找除了3类型外的全部
    List<Find> findFinds(@Param("pageNum") int pageNum,@Param("status") int status);    //查找0类型：失物
//    List<Find> findFindFinds(@Param("status") int status);    //查找1类型：招领
//    List<Find> findPeriodFinds(@Param("status") int status);    //查找2类型：公示
//    List<Find> findSuccessFinds(@Param("status") int status);    //查找3类型：成功案例
//    List<Find> findGoodsByName(@Param("Search") String Search);  //模糊查找
    /**
     *  上传类型
     * @param find 物品
     * @return 返回true上传成功 false上传失败
     */
    boolean addFinds(@Param("find") Find find);

    Find lookFind(@Param("findId") int findId);
    /**
     *  修改物品品类型（status：0，1，2，3之间的转换）
     */
    boolean modifyFindToPeriod(@Param("findId") int findId);
    boolean modifyPeriodToEnd(@Param("findId") int findId);
    //------------------end--------------------//



    //------------------用户模块--------------------//
    List<Find> ShowUserFind(@Param("userId") String userId);//查找用户发布的
    List<Find> ShowUserOp(@Param("userId") String userId);//查找用户有关的

    //------------------end--------------------//
}
