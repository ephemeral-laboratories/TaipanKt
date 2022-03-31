package garden.ephemeral.games.taipan.model

// Logical position of a ship by Row/Column.
// We don't care what coordinate space your view is working in.
class LorchaPosition(val row: Int, val column: Int) {
    init {
        if (column < 0 || column > 4 || row < 0 || row > 1) {
            throw IllegalArgumentException("Bogus ship position: $column, $row")
        }
    }

    companion object {
        val All = listOf(
            LorchaPosition(0, 0),
            LorchaPosition(0, 1),
            LorchaPosition(0, 2),
            LorchaPosition(0, 3),
            LorchaPosition(0, 4),
            LorchaPosition(1, 0),
            LorchaPosition(1, 1),
            LorchaPosition(1, 2),
            LorchaPosition(1, 3),
            LorchaPosition(1, 4)
        )

        val AllBackwards = All.reversed()
    }
}
