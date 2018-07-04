package ucab.ingsw.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ucab.ingsw.dataApis.instagramData.InstagramDataUrls;
import ucab.ingsw.dataApis.instagramData.InstagramUrl;
import ucab.ingsw.dataApis.spotifyData.SpotifyData;
import ucab.ingsw.dataApis.spotifyData.SpotifyTracks;
import ucab.ingsw.dataApis.spotifyData.Token;
import ucab.ingsw.dataApis.youtubeData.YoutubeDataUrls;
import ucab.ingsw.dataApis.youtubeData.YoutubeUrl;
import ucab.ingsw.repository.UserRepository;
import ucab.ingsw.response.*;
import ucab.ingsw.model.User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j

@Data
@Service("ApisService")
public class ApisService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public static final String CREDENTIALS_SPOTIFY= "OTM4ZGUxNDc1OTFmNGFhMmFjNDc" +
            "1N2Q0NWNhNDBjNmU6Y2NmOTQ3Mjk3NTBiNGI5NGEzNWY3M2FhMTVmN2NiMGM=";

    public static String ACCESS_TOKEN="TEMPLATE" ;
    public static final String TOTAL_RESULTS_FULL="5";
    public static final String TOTAL_RESULTS_URL="10";

    public NotifyResponse buildNotifyResponse(String message) {
        NotifyResponse respuesta = new NotifyResponse();
        respuesta.setMessage(message);
        respuesta.setTimestamp(LocalDateTime.now());
        return respuesta;
    }


    public ResponseEntity<Object> searchApiContent( String searchTerm, String id){
        User u = userService.searchUserById(id);
        String apiAddress1 = "https://api.instagram.com/v1/tags/"+searchTerm+"/media/recent?access_token="+ u.getInstagramToken();
        String apiAddress2="https://api.spotify.com/v1/search?type=track&q="+searchTerm+"&limit="+TOTAL_RESULTS_FULL;
        String apiAddress3= "https://www.googleapis.com/youtube/v3/search?part=snippet&q="+searchTerm+"&key=AIzaSyBnZJwOtyGQZtE5epo1MR-fYht1p6XW1V8&maxResults=10";
        ACCESS_TOKEN = generarToken();
        List<YoutubeDataUrls> youtubeUrl;
        List<InstagramDataUrls> dataPackage;
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers= new HttpHeaders();
        headers.add("Authorization","Bearer "+ACCESS_TOKEN);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<SpotifyData> response=restTemplate2.exchange(apiAddress2,HttpMethod.GET,request,SpotifyData.class);
        SpotifyData spotifyData =response.getBody();
        RestTemplate restTemplate = new RestTemplate();
        InstagramUrl instagramInfo = restTemplate.getForObject(apiAddress1, InstagramUrl.class);
        dataPackage = instagramInfo.getData();
        YoutubeUrl youtubeInfo = restTemplate.getForObject(apiAddress3, YoutubeUrl.class);
        youtubeUrl = youtubeInfo.getItems();
        if(dataPackage.isEmpty() && spotifyData.getTracks().getItems().isEmpty() && youtubeUrl.isEmpty()){
            log.info("Search has not been sucessfull");
            return ResponseEntity.badRequest().body(buildNotifyResponse("no_result."));
        }
        else {
            return ResponseEntity.ok(buildResponseApis(spotifyData.getTracks(), dataPackage, youtubeUrl));
            }
        }


    private ApiResponse buildResponseApis(SpotifyTracks spotifyTracks, List<InstagramDataUrls> dataPackage, List<YoutubeDataUrls>  youtubeData){
        ApiResponse apiResponse = new ApiResponse();
        List<String> instagramUrls = new ArrayList<>();
        dataPackage.forEach(i -> {
            instagramUrls.add(i.getImages().getStandard_resolution().getUrl());
        });
        List<VideoResponse> videoResponses = new ArrayList<>();
        youtubeData.forEach( i-> {
                    VideoResponse videoResponse =new VideoResponse();
                    videoResponse.setTitle(i.getSnippet().getTitle());
                    if (i.getId().getVideoId()==null){
                    videoResponse.setVideoUrl("NOT_AVAILABLE");
                    }
                    else{
                        videoResponse.setVideoUrl("https://www.youtube.com/watch?v="+i.getId().getVideoId());
                    }
                    videoResponse.setChannelUrl("https://www.youtube.com/channel/"+i.getSnippet().getChannelId()+"?view_as=subscriber");
                    videoResponse.setChannelTitle(i.getSnippet().getChannelTitle());
                    videoResponse.setDescription(i.getSnippet().getDescription());
                    videoResponse.setPublishedAt(i.getSnippet().getPublishedAt());
                    videoResponses.add(videoResponse);
                }
        );
        List<SongResponse> trackResponses = new ArrayList<>();
        spotifyTracks.getItems().forEach( i-> {
                    SongResponse songResponse =new SongResponse();
                    songResponse.setName(i.getName());
                    songResponse.setAlbum(i.getAlbum().getName());
                    songResponse.setAlbumImageUrl(i.getAlbum().getImages().get(1).getUrl());
                    List<String> artists = new ArrayList<>();
                    i.getArtists().forEach( j->{
                                artists.add(j.getName());
                            }
                    );
                    songResponse.setArtists(artists);
                    songResponse.setUrl(i.getExternal_urls().getSpotify());
                    trackResponses.add(songResponse);
                }
        );
        SubApiResponse subApiResponse = new SubApiResponse();
        SpotifyResponse spotifyResponse = new SpotifyResponse();
        InstagramResponse instagramResponse = new InstagramResponse();
        YoutubeResponse youtubeResponse = new YoutubeResponse();
        spotifyResponse.setTracks(trackResponses);
        instagramResponse.setUrls((instagramUrls));
        youtubeResponse.setVideos(videoResponses);
        subApiResponse.setInstagramResponse(instagramResponse);
        subApiResponse.setYoutubeResponse(youtubeResponse);
        subApiResponse.setSpotifyResponse(spotifyResponse);
        apiResponse.setApis(subApiResponse);
        return apiResponse;
    }

    public String generarToken() {
        String tokenUrl="https://accounts.spotify.com/api/token";
        RestTemplate tokenTemplate =new RestTemplate();
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        headers.add("Authorization","Basic "+CREDENTIALS_SPOTIFY);
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("grant_type","client_credentials");
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity(params,headers);
        ResponseEntity<Token> tokenResponse=tokenTemplate.exchange(tokenUrl,HttpMethod.POST,tokenRequest,Token.class);
        Token token =tokenResponse.getBody();
        ACCESS_TOKEN= token.getAccess_token();
        return ACCESS_TOKEN;
    }

}
