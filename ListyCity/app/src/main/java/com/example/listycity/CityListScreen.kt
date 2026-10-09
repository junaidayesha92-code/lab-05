package com.example.listycity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    onDeleteCity: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var editedCityName by remember { mutableStateOf("") }
    var editedProvinceName by remember { mutableStateOf("") }
    var showOptions by remember { mutableStateOf(false) }
    var showEditFields by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showAddCityFields = !showAddCityFields
                    if (showAddCityFields) {
                        selectedCity = null
                        showOptions = false
                        showEditFields = false
                        editedCityName = ""
                        editedProvinceName = ""
                    }
                }
            ) {
                Text("+")
            }
        }
        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            onAddCity(
                                City(
                                    name = newCityName,
                                    province = newProvinceName
                                )
                            )

                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    }
                ) {
                    Text("ADD CITY")
                }
            }
        }
        if (selectedCity != null && showEditFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editedCityName,
                    onValueChange = { editedCityName = it },
                    label = { Text("Updated City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = editedProvinceName,
                    onValueChange = { editedProvinceName = it },
                    label = { Text("Updated Province") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        val cityToUpdate = selectedCity
                        if (
                            cityToUpdate != null &&
                            editedCityName.isNotBlank() &&
                            editedProvinceName.isNotBlank()
                        ) {
                            onUpdateCity(
                                cityToUpdate,
                                City(
                                    name = editedCityName,
                                    province = editedProvinceName
                                )
                            )

                            selectedCity = null
                            showEditFields = false
                            editedCityName = ""
                            editedProvinceName = ""
                        }
                    }
                ) {
                    Text("UPDATE CITY")
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                CityRow(
                    city = city,
                    onClick = {
                        showEditFields = false
                        showOptions = true
                        showAddCityFields = false
                        newCityName = ""
                        newProvinceName = ""
                        selectedCity = city
                        editedCityName = city.name
                        editedProvinceName = city.province
                    }
                )
                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }

        val cityForOptions = selectedCity
        if (showOptions && cityForOptions != null) {
            AlertDialog(
                onDismissRequest = {
                    showOptions = false
                    selectedCity = null
                },
                title = { Text(cityForOptions.name) },
                text = { Text("What would you like to do?") },
                confirmButton = {
                    Button(onClick = {
                        showOptions = false
                        showEditFields = true
                    }) {
                        Text("UPDATE")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        onDeleteCity(cityForOptions)
                        showOptions = false
                        selectedCity = null
                        editedCityName = ""
                        editedProvinceName = ""
                    }) {
                        Text("DELETE")
                    }
                }
            )
        }
    }
}


@Composable
fun CityRow(
    city: City,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCityTheme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> },
            onDeleteCity = {}
        )
    }
}