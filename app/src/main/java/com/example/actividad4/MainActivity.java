package com.example.actividad4;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText etProductCode, etProductDescription, etProductPrice;
    Button btnAdd, btnSearch, btnUpdate, btnDelete;
    ListView lvProducts;
    ArrayAdapter<String> adapter;
    ArrayList<String> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        etProductCode = findViewById(R.id.etProductCode);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);

        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        lvProducts = findViewById(R.id.lvProducts);

        productList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        lvProducts.setAdapter(adapter);

        addProduct();
        searchProduct();
        updateProduct();
        deleteProduct();
        loadProducts();
    }

    public void addProduct() {
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(
                                Integer.parseInt(etProductCode.getText().toString()),
                                etProductDescription.getText().toString(),
                                Double.parseDouble(etProductPrice.getText().toString())
                        );
                        if (isInserted) {
                            Toast.makeText(MainActivity.this, "Producto Agregado", Toast.LENGTH_LONG).show();
                            loadProducts();
                        } else {
                            Toast.makeText(MainActivity.this, "Error al Agregar Producto", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void searchProduct() {
        btnSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getData(Integer.parseInt(etProductCode.getText().toString()));
                        if (res.getCount() == 0) {
                            Toast.makeText(MainActivity.this, "Producto no Encontrado", Toast.LENGTH_LONG).show();
                            return;
                        }
                        while (res.moveToNext()) {
                            etProductDescription.setText(res.getString(2));
                            etProductPrice.setText(res.getString(3));
                        }
                    }
                }
        );
    }

    public void updateProduct() {
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(
                                Integer.parseInt(etProductCode.getText().toString()),
                                Integer.parseInt(etProductCode.getText().toString()),
                                etProductDescription.getText().toString(),
                                Double.parseDouble(etProductPrice.getText().toString())
                        );
                        if (isUpdate) {
                            Toast.makeText(MainActivity.this, "Producto Actualizado", Toast.LENGTH_LONG).show();
                            loadProducts();
                        } else {
                            Toast.makeText(MainActivity.this, "Error al Actualizar Producto", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void deleteProduct() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(Integer.parseInt(etProductCode.getText().toString()));
                        if (deletedRows > 0) {
                            Toast.makeText(MainActivity.this, "Producto Eliminado", Toast.LENGTH_LONG).show();
                            loadProducts();
                        } else {
                            Toast.makeText(MainActivity.this, "Error al Eliminar Producto", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void loadProducts() {
        productList.clear();
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No se encontraron productos", Toast.LENGTH_LONG).show();
            return;
        }
        while (res.moveToNext()) {
            productList.add("Código: " + res.getString(1) + ", Descripción: " + res.getString(2) + ", Precio: " + res.getString(3));
        }
        adapter.notifyDataSetChanged();
    }
}
