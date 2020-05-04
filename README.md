# Food-Sorting-Android-App

## Description
I created a nutrition app that monitors and tracks your diet. The app uses a guiding stars algorithm to categorize the foods as "good" or "bad" and you have the ability to scan the barcodes of the foods you eat to keep track of your diet (this includes caloric intake, protein, carbohydrates, and fats).

## Primary App Descriptors
The layout of the app is a tab bar and uses a recyclerview in order to display cards which have information about the certain types of foods in the diet. The main part of the app revolves around barcode scanning; using google vision, I scanned barcodes and sent it to nutritionix in order to receive information about the item. From there, I implemented google custom search api in order to retrieve an image that corresponds with the item scanned by the barcode. I also used a surfaceview to permanently have a camera on when the fragment is opened. The app allows you to save favorite items to a different fragment and the app saves the diets until it is manually cleared by the user.
