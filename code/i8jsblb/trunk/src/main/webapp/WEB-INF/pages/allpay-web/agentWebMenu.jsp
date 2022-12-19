<script type="text/javascript" id="demo1-javascript">
    var jq = jQuery.noConflict();
    jq(document).ready(function () {
        function prepareList() {
            jq('#expList').find('li:has(ul)')
                    .click(function (event) {
                        if (this == event.target) {
                            jq(this).toggleClass('expanded');
                            jq(this).children('ul').toggle('medium');
                        }
                        return false;
                    })
                    .addClass('collapsed')
                    .children('ul').hide();
        };

        // Initialize navgoco with default options
        jq("#demo1").navgoco({
            caretHtml: '',
            accordion: false,
            openClass: 'open',
            save: true,
            cookie: {
                name: 'navgoco',
                expires: false,
                path: '/'
            },
            slide: {
                duration: 400,
                easing: 'swing'
            },
            // Add Active class to clicked menu item
            onClickAfter: function (e, submenu) {
                e.preventDefault();
                jq('#demo1').find('li').removeClass('active');
                var li = jq(this).parent();
                var lis = li.parents('li');
                var liss = lis.parents('ul');
                li.addClass('active');
                lis.addClass('active');
                liss.addClass('active');
            },
        });

        jq("#collapseAll").click(function (e) {
            e.preventDefault();
            jq("#demo1").navgoco('toggle', false);
        });

        jq("#expandAll").click(function (e) {
            e.preventDefault();
            jq("#demo1").navgoco('toggle', true);
        });

        jq("#demo1").navgoco({accordion: true});
        jq("#demo1").navgoco('toggle', false);
    });
</script>
<%@include file="/common/taglibs.jsp" %>
<%@ page language="java" %>
<input type="hidden" id="contextpath" value='<%=request.getRequestURI()%>'>
<ul id="demo1" class="nav">
    <c:if test="${not empty categories}">
        <c:forEach var="menu" items="${categories}">

            <li><a href="#">${menu.name}</a>

                <ul>
                    <c:forEach var="subMenu" items="${menu.childCategory}">
                        <li>


                        <c:if test="${subMenu.productList.size() > 1}">
                            <a href="#">${subMenu.name}</a>
                        </c:if>

                        <c:if test="${subMenu.productList.size() eq 1}">
                            <a href="${subMenu.productList[0].url}">${subMenu.name}</a>
                        </c:if>


                            <c:if test="${not empty subMenu.childCategory}">
                                <ul>
                                    <c:forEach var="subMenuCategory" items="${subMenu.childCategory}">
                                        <li><a href="#">${subMenuCategory.name}
                                        </a>

                                            <c:if test="${not empty subMenuCategory.productList}">
                                                <ul>
                                                    <c:forEach var="subMenuProduct"
                                                               items="${subMenuCategory.productList}">
                                                        <a href=${subMenuProduct.url}>
                                                            <li>${subMenuProduct.name}

                                                            </li>
                                                        </a>
                                                    </c:forEach>
                                                </ul>
                                            </c:if>

                                        </li>

                                    </c:forEach>
                                </ul>
                            </c:if>

                            <c:if test="${not empty subMenu.productList and subMenu.productList.size() > 1}">
                                <ul>
                                    <c:forEach var="subMenuProduct" items="${subMenu.productList}">
                                        <a href=${subMenuProduct.url}>
                                            <li>${subMenuProduct.name}

                                            </li>
                                        </a>
                                    </c:forEach>
                                </ul>
                            </c:if>
                        </li>
                    </c:forEach>
                    <c:if test="${not empty menu.productList}">
                        <c:forEach var="subMenuProduct" items="${menu.productList}">


                            <a href=${subMenuProduct.url}>
                                <li>${subMenuProduct.name}

                                </li>
                            </a>
                        </c:forEach>


                    </c:if>

                </ul>


            </li>

        </c:forEach>


    </c:if>
</ul>
<p class="external">
    <a href="#" id="collapseAll">Collapse All</a> | <a href="#" id="expandAll">Expand All</a>
</p>
