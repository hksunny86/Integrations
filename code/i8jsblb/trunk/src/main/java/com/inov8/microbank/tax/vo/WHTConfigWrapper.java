package com.inov8.microbank.tax.vo;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.tax.model.WHTConfigModel;
import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malik on 7/1/2016.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WHTConfigWrapper extends BasePersistableModel implements Serializable
{
        private static final long	serialVersionUID	= 1L;
        private List<WHTConfigVo> whtConfigVoList;

    public WHTConfigWrapper()
        {
            super();
            whtConfigVoList = LazyList.decorate(new ArrayList<WHTConfigVo>(), FactoryUtils.instantiateFactory(WHTConfigVo.class));
        }

        @Override
        public void setPrimaryKey(Long aLong)
        {

        }

        @Override
        public Long getPrimaryKey()
        {
            return null;
        }

        @Override
        public String getPrimaryKeyParameter()
        {
            return null;
        }

        @Override
        public String getPrimaryKeyFieldName()
        {
            return null;
        }

        public List<WHTConfigVo> getWhtConfigVoList()
        {
            return whtConfigVoList;
        }

        public void setWhtConfigVoList(List<WHTConfigVo> whtConfigVoList)
        {
            this.whtConfigVoList = whtConfigVoList;
        }

        public void addWHTConfigVo(WHTConfigVo whtConfigVo){
            this.whtConfigVoList.add(whtConfigVo);

        }

    }

