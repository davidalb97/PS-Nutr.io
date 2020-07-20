package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper

//class UserMapper : RowMapper<DbUserDto> {
//
//    override fun map(rs: ResultSet?, ctx: StatementContext?): DbUserDto {
//        return DbUserDto(
//                submitter_id = rs!!.getInt(UserDao.id),
//                submitter_name = rs.getString(UserDao.email),
//                session_secret = rs.getString(UserDao.sessionSecret),
//                email = rs.getString(UserDao.email),
//                creation_date = parsePostgresql(rs.getString(SubmitterDao.date))
//        )
//    }
//}