package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

interface IResponseMapper<Dto, Model> {

    fun map(dto: Dto) : Model

}