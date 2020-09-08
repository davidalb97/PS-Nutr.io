package pt.isel.ps.g06.httpserver.exception.problemJson.badRequest

import pt.isel.ps.g06.httpserver.model.InsulinProfile

class OverlappingInsulinProfilesException(private val overlappingProfile: InsulinProfile) : BaseBadRequestException(
        title = "Given insulin profile overlaps with others!",
        detail = "Insulin profiles cannot have overlapping time intervals, " +
                "and given one already clashes with Profile#${overlappingProfile.identifier} " +
                "with interval of ${overlappingProfile.startTime} - ${overlappingProfile.endTime}"
)