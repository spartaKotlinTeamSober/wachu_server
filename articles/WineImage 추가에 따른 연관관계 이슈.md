# WineImage 추가에 따른 연관관계 이슈

## 문제

`Wine Image`를 불러오는 기능이 생김에 따라 연관관계에 있는 모든 `Reponse`가 `Url`정보를 받아와야하는 문제가 있었음. 

그냥 각각의 서비스에서 url로직을 만들면 그만이겠지만, 코드적으로도 그렇고, 관심사 적으로도 올바르진 않다로 여겨졌음. 

또 그렇다고 `wine response`에서 `mediaService`같은 걸 주입받기도 이상함

## 해결방법

```jsx
object WineImageGetter {
    private lateinit var mediaS3Service: MediaS3Service

    fun getWineImage(wineType: WineType): String {
        return mediaS3Service.getS3Image(S3FilePath.WINE.path + wineType.path)
            .random()
    }

    fun init(mediaS3Service: MediaS3Service) {
        this.mediaS3Service = mediaS3Service
    }
}
```

`wine image getter`를 싱글톤으로 만들어서 
`companion object` 에서 호출 가능하록 함. 

그리고

```jsx
 @PostConstruct
    fun init() {
        WineImageGetter.init(this)
    }
```

`mediaService`의 `Been`주입과 생성이 완료되면 이것을 `WineImageGetter`에게 수동으로 `init`하여 의존 가능하도록 함.

## 결과

```jsx
  companion object {

        fun from(entity: Wine): WineResponse {
            return WineResponse(
                id = entity.id,
                name = entity.name,
                sweetness = entity.sweetness,
                acidity = entity.acidity,
                body = entity.body,
                tannin = entity.tannin,
                wineType = entity.wineType,
                aroma = entity.aroma,
                price = entity.price,
                kind = entity.kind,
                style = entity.style,
                country = entity.country,
                region = entity.region,
                imageUrl = WineImageGetter.getWineImage(entity.wineType)
            )
        }
    }
```

이것으로 다른 서비스에서도 `wineImageUrl`을 호출할 필요 없이

`WineReponse` 내부에서 실행되도록 하여 코드적 이점을 챙겼음
