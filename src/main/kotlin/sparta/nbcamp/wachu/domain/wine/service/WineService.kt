package sparta.nbcamp.wachu.domain.wine.service

import sparta.nbcamp.wachu.domain.wine.dto.WineResponse

interface WineService {
    fun getWineList(): List<WineResponse>
    fun getWineById(wineId: Long): WineResponse
    fun compareWine(wineIds: List<Long>): List<WineResponse>
}