package com.inov8.microbank.server.dao.productmodule.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogDAO;

/**
 * <p>Title: The microbank Project</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public class ProductCatalogHibernateDAO
    extends BaseHibernateDAO<ProductCatalogModel, Long, ProductCatalogDAO>
    implements ProductCatalogDAO
{
  String productCatalogWithDetailsQuery = "from ProductCatalogModel pc left join fetch pc.productCatalogIdProductCatalogDetailModelList where pc.productCatalogId = ? ";
  String query = "FROM ProductCatalogModel a WHERE a.appUserTypeModel.appUserTypeId = 3 AND a.active = TRUE ";
  public ProductCatalogModel loadProductCatalogModelWithDetailsById(Long
      productCatalogId)
  {
    List<ProductCatalogModel>
        list = this.getHibernateTemplate().find(productCatalogWithDetailsQuery,
                                                productCatalogId);
    if (null != list && list.size() > 0)
    {
      return list.get(0);
    }
    return null;

  }

  /*
    Method loadCatalog depends on HQL query because for product catalog edit
   case, we need to follow the link that: ProductCatalog -> ProductCatalogDetail
   -> Product -> Supplier


   */
  public List<ProductCatalogModel> fetchProductCatalog(ProductCatalogModel productCatalogModel){
    List<ProductCatalogModel>
            list = this.getHibernateTemplate().find(query);
    if (null != list && list.size() > 0)
    {
      return list;
    }
    return null;
  }

  public ProductCatalogModel loadCatalog(Long productCatalogId) throws
      FrameworkCheckedException
  {

    try
    {
      Session hibernateSession = this.getSession();
      String query = "from ProductCatalogModel pc left join fetch pc.productCatalogIdProductCatalogDetailModelList pcDetail " +
          "left join fetch pcDetail.relationProductIdProductModel prod " +
          "left join fetch prod.relationSupplierIdSupplierModel where pc.productCatalogId =" +
          productCatalogId.intValue() + " order by pcDetail.sequenceNo asc" ;

      Query q = hibernateSession.createQuery(query);
      List result = q.list();

      SessionFactoryUtils.releaseSession(hibernateSession, getSessionFactory());
      return (ProductCatalogModel) result.get(0);

    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new FrameworkCheckedException("Could not extract catalog products.",
                                          e);

    }
  }

  public ProductCatalogModel loadCatalogAll() throws FrameworkCheckedException
  {
    try
    {
      Session hibernateSession = this.getSession();
      String query = "from ProductCatalogModel pc where pc.name = \'ALL\'";

      Query q = hibernateSession.createQuery(query);
      List list = q.list();
      SessionFactoryUtils.releaseSession(hibernateSession, getSessionFactory());
      if (list.size() == 0)
      {
        return null;
      }
      return (ProductCatalogModel) list.get(0);

    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new FrameworkCheckedException("Could not extract catalog products.",
                                          e);

    }

  }

}
