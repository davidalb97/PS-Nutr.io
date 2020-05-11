package pt.ipl.isel.leic.ps.androidclient.data.source.mapper

interface IArrayResponseMapper<Dto, Model> {

    fun map(dtos: Array<Dto>) : Array<Model>

}