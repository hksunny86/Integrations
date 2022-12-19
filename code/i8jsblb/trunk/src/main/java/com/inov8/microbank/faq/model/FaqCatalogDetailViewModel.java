package com.inov8.microbank.faq.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "FAQ_CATALOG_DETAIL_VIEW")
public class FaqCatalogDetailViewModel extends BasePersistableModel implements Serializable
{

	
	private static final long serialVersionUID = 1L;
	private Long pk;
	private Long faqCatalogId;
	private String faqCatalogName;
	private Long faqId;
	private Long questionNo;
	private String question;
	private String answer;
	private Long faqCategoryId;
	private String faqCategoryName;
	
	@Column(name = "PK"  )
	@Id
	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	@Column(name = "FAQ_CATALOG_ID"  )
	public Long getFaqCatalogId() {
		return faqCatalogId;
	}

	public void setFaqCatalogId(Long faqCatalogId) {
		this.faqCatalogId = faqCatalogId;
	}

	@Column(name = "FAQ_CATALOG_NAME"  )
	public String getFaqCatalogName() {
		return faqCatalogName;
	}

	public void setFaqCatalogName(String faqCatalogName) {
		this.faqCatalogName = faqCatalogName;
	}

	@Column(name = "FAQ_ID"  )
	public Long getFaqId() {
		return faqId;
	}

	public void setFaqId(Long faqId) {
		this.faqId = faqId;
	}

	@Column(name = "QUESTION_NO"  )
	public Long getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(Long questionNo) {
		this.questionNo = questionNo;
	}

	@Column(name = "QUESTION"  )
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Column(name = "ANSWER"  )
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Column(name = "FAQ_CATEGORY_ID"  )
	public Long getFaqCategoryId() {
		return faqCategoryId;
	}

	public void setFaqCategoryId(Long faqCategoryId) {
		this.faqCategoryId = faqCategoryId;
	}

	@Column(name = "FAQ_CATEGORY_NAME"  )
	public String getFaqCategoryName() {
		return faqCategoryName;
	}

	public void setFaqCategoryName(String faqCategoryName) {
		this.faqCategoryName = faqCategoryName;
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() 
	{
		String parameters = "";
		parameters += "&pk=" + getPk();
		return parameters;
	}
		
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName()
	{ 
		String primaryKeyFieldName = "pk";
		return primaryKeyFieldName;				
    }

	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() 
	{
		return getPk();
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) 
	{
		setPk(primaryKey);
    }
	
}
