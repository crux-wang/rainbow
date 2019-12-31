# 文章模块
> context text

`/articles`

## 1.  创建

**POST**   `/articles`  // context text inline

**request body**
	
	Article // context text inline
	
**response**
	
	Article // context text inline
	 
## 2.  查询

**GET**   `/articles`

**request param**

|  name | type | required | comment text |
| ------------ | ------------ | ------------ | ------------ |
|  keyword | String  | N | context text inline |
| pageable |  Pageable | Y | context text inline |

**response**
	
	 Page[Article] // context text inline

# 实体列表

##  Article

|  name | type | comment text |
| ------------ | ------------ | ------------ |
|  id | String  | context text inline |
| title |  String | context text inline |
| content |  String | context text inline |
| createTime |  String | context text inline |

