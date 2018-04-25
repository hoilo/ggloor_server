package testgrails12

import grails.converters.JSON
import org.grails.datastore.mapping.query.Query.In

class RestController {
    def authorizationService
    def index(String page) {
        render 'RestController ' + page
    }
    def GetMatches(Integer page){
        System.out.println(page)
        def matches = Matches.findAll('from Matches order by dateMatch desc',[max:10 * page])
        JSON.use('deep')
        render matches as JSON
    }
    def GetLikes(String key){
        System.out.println(key)
        def kl = KeyLogger.findByKeyStr(key)
        if(kl != null){
            def likes = FavoriteTeams.findAll("from FavoriteTeams where user = ?", [kl.user])
            JSON.use('deep')
            render likes as JSON
        }else {
            render '0'
        }
    }
    def GetOneMatchInfo(Integer id){
        System.out.println('GetOneMatchInfo: ' + id)
        def games = Games.findAll("from Games where match_id = ?", [id])
        JSON.use('deep')
        render games as JSON
    }
    def GetComments(Integer id){
        System.out.println("GetComments: " + id);
        def comments = Comments.findAll("from Comments where match_id = ?", [id])
        JSON.use('deep')
        render comments as JSON
    }
    def sendComment(Integer match_id, String comment, String key){
        def login = authorizationService.testAuth(key)
        if(login != null){
            def ncomment = new Comments(user: login, text: comment, match: Matches.get(match_id), date: new Date())
            ncomment.save()
            render "ok"
        }
    }
}
