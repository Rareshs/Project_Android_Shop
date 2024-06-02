package com.example.project_app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActionBarFragment extends Fragment {
    ImageButton btnHome, btnShopping_Cart, btnFavorite, btnAccount;
    View underline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_action_bar, container, false);
        // Initialize buttons
        btnHome = rootView.findViewById(R.id.btnHome);
        btnShopping_Cart = rootView.findViewById(R.id.btnShoppingCart);
        btnFavorite = rootView.findViewById(R.id.btnFavorites);
        btnAccount = rootView.findViewById(R.id.btnAccount);
        underline = rootView.findViewById(R.id.underline);

        // Set onClickListeners for buttons
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with HomeFragment
                replaceFragment(new HomeFragment(), "HOME_FRAGMENT_TAG");
                moveUnderline(btnHome); // Move underline to clicked button
                Toast.makeText(getActivity(), "Home button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnShopping_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with ShoppingCartFragment
                replaceFragment(new ShoppingCartFragment(), "SHOPPING_CART_FRAGMENT_TAG");
                moveUnderline(btnShopping_Cart); // Move underline to clicked button
                Toast.makeText(getActivity(), "ShoppingCart button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with FavoriteFragment
                replaceFragment(new FavoriteFragment(), "FAVORITE_FRAGMENT_TAG");
                moveUnderline(btnFavorite); // Move underline to clicked button
                Toast.makeText(getActivity(), "Fav button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with Account_Fragment
                replaceFragment(new Account_Fragment(), "ACCOUNT_FRAGMENT_TAG");
                moveUnderline(btnAccount); // Move underline to clicked button
                Toast.makeText(getActivity(), "Account button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void moveUnderline(View clickedButton) {
        int[] location = new int[2];
        clickedButton.getLocationOnScreen(location);
        int left = location[0];

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) underline.getLayoutParams();
        params.leftMargin = left;
        params.width = clickedButton.getWidth();
        underline.setLayoutParams(params);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("ActionBarFragment", "onCreateOptionsMenu called");
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("ActionBarFragment", "onOptionsItemSelected called");

        int itemId = item.getItemId();
        if (itemId == R.id.exit) {
            Toast.makeText(getActivity(), "Settings option clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.settings) {
            showExitConfirmationDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requireActivity().finish();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);

        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }

        dialog.show();
    }
}
