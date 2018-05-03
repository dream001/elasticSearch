package elasticSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;

public class ESQueryBuilders implements ESCriterion {

    private List<QueryBuilder> list = new ArrayList<QueryBuilder>(); 

    public List<QueryBuilder> listBuilders() {
        // TODO Auto-generated method stub
        return list;
    }
    
    /**
     * @Title: term
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param field
     * @param value
     * @return
     */
    public ESQueryBuilders term(String field, Object value) {  
        list.add(new ESSimpleExpression (field, value, Operator.TERM).toBuilder());  
        return this;  
    }
    
    /**
     * 
     * @Title: terms
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param field
     * @param values
     * @return
     */
    public ESQueryBuilders terms(String field, Collection<Object> values) {  
        list.add(new ESSimpleExpression (field, values).toBuilder());  
        return this;  
    } 
    
    public ESQueryBuilders fuzzy(String field, Object value) {  
        list.add(new ESSimpleExpression (field, value, Operator.FUZZY).toBuilder());  
        return this;  
    } 
    
    
    public ESQueryBuilders range(String field, Object from, Object to) {  
        list.add(new ESSimpleExpression (field, from, to).toBuilder());  
        return this;  
    } 
    
    
    public ESQueryBuilders queryString(String queryString) {  
        list.add(new ESSimpleExpression (queryString, Operator.QUERY_STRING).toBuilder());  
        return this;  
    } 
}
