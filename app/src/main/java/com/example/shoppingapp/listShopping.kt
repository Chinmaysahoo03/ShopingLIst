package com.example.shoppingapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingItem(val id:Int ,var name:String,var Quantity:Int, var isEditing:Boolean=false)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAppp()
{
    var Sitem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showdialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column( modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Button(onClick = { showdialog=true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(30.dp))
        {
            Text(text = "ADD ITEM +")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(Sitem)
            {
                item ->
                if(item.isEditing)
                {
                    ShoppingItemEditor(item =item, onEditComplete = {
                        editedName,editedQuantity -> Sitem=Sitem.map{it.copy(isEditing = false)}
                       val editedItem= Sitem.find{it.id==item.id}
                        editedItem?.let {
                            it.name=editedName
                            it.Quantity=editedQuantity
                        }
                    } )
                }
                else
                {
                   ListItems(item = item, onEdit = { Sitem=Sitem.map { it.copy(isEditing = it.id==item.id) }},
                       onDelete ={Sitem=Sitem-item} )
                }
            }
        }

    } 
    if(showdialog)
    {
        AlertDialog(onDismissRequest = { showdialog=false},
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween)
                            {
                                Button(onClick = {
                                    if(itemName.isNotBlank())
                                    {
                                        val new=ShoppingItem(
                                            id=Sitem.size+1,
                                            name=itemName,
                                            Quantity = itemQuantity.toInt()
                                        )
                                        Sitem=Sitem+new
                                        showdialog=false
                                        itemName=""
                                        itemQuantity=""
                                    }
                                }) {
                                    Text(text = "Add")
                                }
                                Button(onClick = { showdialog=false }) {
                                    Text(text = "Cancel")
                                }
                            }
            },
            title = { Text(text = "ADD SHOPPING ITEMS")},
            text = {
                Column {
                    OutlinedTextField(value = itemName ,
                        onValueChange ={itemName=it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp))
                    OutlinedTextField(value = itemQuantity,
                        onValueChange ={itemQuantity=it},
                        singleLine = true,
                        modifier = Modifier.padding(16.dp))
                }
            }
            )
    }
}
@Composable
fun ListItems(
    item:ShoppingItem,
    onEdit:() -> Unit,
    onDelete:() -> Unit,
)
{
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = "item:${item.name}", modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "Qty: ${item.Quantity}", modifier = Modifier.padding(8.dp),fontWeight = FontWeight.Bold,fontSize = 20.sp)
        Row (
            modifier = Modifier.padding(8.dp),
        ){
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon")
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete Icon")
            }
        }
    }
    
}
@Composable
fun ShoppingItemEditor(item: ShoppingItem,onEditComplete:(String,Int)->Unit)

{
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.Quantity.toString() )}
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Column {
            BasicTextField(value = editedName,
                onValueChange ={editedName=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(value = editedQuantity,
                onValueChange ={editedQuantity=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }
        Button(onClick = { isEditing=false
        onEditComplete(editedName,editedQuantity.toInt())
        })
        {
         Text(text = "Save")
        }
    }

}
