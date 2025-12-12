package com.example.huellapp

import app.cash.turbine.test
import com.example.huellapp.DAO.PerroDao
import com.example.huellapp.model.Perro
import com.example.huellapp.repository.PerroRepository
import com.example.huellapp.viewmodel.PerroViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)

class PerroViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var perroDao: PerroDao
    private lateinit var repository: PerroRepository
    private lateinit var viewModel: PerroViewModel

    @Before
    fun setup() {
        perroDao = mockk(relaxed = true)

        every { perroDao.obtenerPerros() } returns flowOf(emptyList())

        repository = PerroRepository(perroDao)
        viewModel = PerroViewModel(repository)
    }

    @Test
    fun `perros flow emite lista inicial`() = runTest {
        val listaInicial = listOf(
            Perro(1, "Bobby", "Labrador", 3, 15.2, "Tranquilo"),
            Perro(2, "Nina", "Poodle", 5, 7.4, "Juguetona")
        )

        every { perroDao.obtenerPerros() } returns flowOf(listaInicial)

        repository = PerroRepository(perroDao)
        viewModel = PerroViewModel(repository)

        viewModel.perros.test {
            val resultado = awaitItem()
            assertEquals(2, resultado.size)
            assertEquals("Bobby", resultado[0].nombre)
            assertEquals("Nina", resultado[1].nombre)
        }
    }

    @Test
    fun `agregarPerro llama DAO insert`() = runTest {
        val perro = Perro(
            nombre = "Rocky",
            raza = "Pastor Alemán",
            edad = 4,
            peso = 24.5,
            temperamento = "Firme"
        )

        viewModel.agregarPerro(perro)

        coVerify(exactly = 1) { perroDao.insertarPerro(perro) }
    }

    @Test
    fun `eliminarPerro llama DAO delete`() = runTest {
        val perro = Perro(
            nombre = "Luna",
            raza = "Beagle",
            edad = 2,
            peso = 8.2,
            temperamento = "Cariñosa"
        )

        viewModel.eliminarPerro(perro)

        coVerify(exactly = 1) { perroDao.eliminarPerro(perro) }
    }

    @Test
    fun `eliminarTodos llama DAO deleteAll`() = runTest {
        viewModel.eliminarTodos()

        coVerify(exactly = 1) { perroDao.eliminarTodos() }
    }
}
