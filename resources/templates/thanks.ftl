<#global page_title = "おてがみ一覧" />
<#import "common/container.ftl" as b>

<@b.page>
    <div class="cards">
        <#if thanks?? && (thanks?size > 0)>
            <#list thanks as thank>
                <a href="/thanks/${thank.id}">
                    <div class="card">

                        <div class="card-to-user">
                            <img class="card_user_image" src="${thank.targetUserImage}" alt="" width="40" height="40"/>
                            <div class="card-to-user-name">${thank.targetRealName}さんへ</div>
                        </div>

                        <div class="card-content">
                            <p>${thank.body}</p>
                        </div>

                        <div class="card-reaction">
                            <div class="card-reaction-count">スレッド${thank.threadCount}件</div>
                        </div>

                        <div class="card-info">
                            <img class="card-from-image" src="${thank.userImage}" alt="" width="30" height="30"/>
                            <div class="card-from-user-name">${thank.realName}より</div>
                        </div>
                    </div>
                </a>
            </#list>
        </#if>
    </div>
</@b.page>