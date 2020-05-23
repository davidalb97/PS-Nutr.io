package pt.ipl.isel.leic.ps.androidclient.data.source.mapper

interface IResponseMapper<Dto, Model> {

    fun map(dto: Dto) : Model

}