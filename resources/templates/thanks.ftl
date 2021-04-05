<#global page_title = "サンクス一覧" />
<#import "common/container.ftl" as container>

<@container.page>
    <div class="thanks">
        <#if thanks?? && (thanks?size > 0)>
            <#list thanks as thank>
                <a href="/thanks/${thank.id}">
                    <div class="thanks-card">

                        <div class="thanks-card-to-user">
                            <img src="${thank.targetUserImage}" alt="" width="40" height="40"/>
                            <p>${thank.targetRealName}さんへ</p>
                        </div>

                        <div class="thanks-card-content">
                            <p>${thank.body}</p>
                        </div>

                        <div class="thanks-card-reaction">
                            <p>スレッド${thank.threadCount}件</p>
                        </div>

                        <div class="thanks-card-from-user">
                            <img src="${thank.userImage}" alt="" width="30" height="30"/>
                            <p>${thank.realName}より</p>
                        </div>
                    </div>
                </a>
            </#list>
        </#if>
    </div>
</@container.page>