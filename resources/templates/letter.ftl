<#import "common/milligram.ftl" as b>

<@b.page>
    <div class="cards">
        <#if thanks?? && (thanks?size > 0)>
            <#list thanks as thank>
                <a href="/letter/detail">
                    <div class="card">

                        <div class="card-to-user">
                            <img class="card_user_image" src="${thank.userImage}" alt="" width="40" height="40"/>
                            <div class="card-to-user-name">${thank.realName}さんへ</div>
                        </div>

                        <div class="card_content">
                            <p>${thank.body}</p>
                        </div>

                        <div class="card_reaction">
                            <img class="card_user_image" src="http://wwwjp.kodak.com/JP/images/ja/digital/cameras/samplePicture/dc21203.jpg" width="20" height="20"/>
                            <div class="card_reaction_count">スレッド10件</div>
                        </div>

                        <div class="card_info">
                            <img class="card-from-image" src="${thank.targetUserImage}" alt="" width="30" height="30"/>
                            <div class="card-from-user-name">ほげこさんより</div>
                        </div>
                    </div>
                </a>
            </#list>
        </#if>
    </div>
</@b.page>