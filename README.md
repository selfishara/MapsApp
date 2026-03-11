# MapsApp

MapsApp is an Android application built with Kotlin and Jetpack Compose that allows authenticated users to create, save and manage map markers with title, description, coordinates and optional images.

## Features

- User authentication with Supabase
- Session persistence with Splash screen
- Interactive map with Google Maps
- Marker creation with title, description and image
- Image upload to Supabase Storage
- Marker persistence in Supabase Database
- "My markers" screen to view saved markers
- Logout flow

## Tech stack

- Kotlin
- Jetpack Compose
- Material 3
- Google Maps Compose
- Supabase Auth
- Supabase Postgrest
- Supabase Storage

## Project structure

```text
com.example.mapsapp
├── core
│   ├── components
│   ├── layout
│   ├── navigation
│   ├── permissions
│   └── theme
├── data
│   ├── model
│   └── remote
├── features
│   ├── auth
│   ├── map
│   ├── marker
│   └── splash
└── utils

Main flows

Authentication
	•	Register
	•	Login
	•	Logout
	•	Session persistence through Splash screen

Marker management
	•	Long press on the map
	•	Create a marker
	•	Add optional image
	•	Save marker in Supabase
	•	View markers in “My markers”

Setup

1. Clone the repository
git clone <YOUR_REPOSITORY_URL>

2. Open in Android Studio

3. Configure local properties

Add the following values in local.properties or secrets.properties:
supabaseUrl=YOUR_SUPABASE_URL
supabaseKey=YOUR_SUPABASE_KEY
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY

4. Configure Supabase

Make sure:
	•	Authentication is enabled
	•	Email provider is enabled
	•	Email confirmation is disabled for development if immediate session is needed
	•	The posts table exists
	•	Storage bucket for images exists
	•	RLS policies are configured correctly

5. Run the project

Use an emulator or Android device with Google Play Services.

Current status

Implemented:
	•	Auth flow
	•	Splash screen
	•	Map screen
	•	Marker creation
	•	Marker image upload
	•	My markers screen
	•	Settings
	•	About
	•	Logout

Pending / future improvements:
	•	Edit markers
	•	Marker detail screen
	•	Profile screen
	•	Custom app branding
	•	Final UI polish

Authors
	•	Sara Martínez Bascuas

Notes

This project was developed as an Android application practice project using modern Android architecture with ViewModels, repositories and Compose UI.