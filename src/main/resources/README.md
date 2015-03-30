# Navigation Toolbar

![Navigation Toolbar Logo](http://www.furdei.systems/img/portfolio/navigation.jpg "Navigation Toolbar Logo")

**Furdroid-navigationtoolbar** contains a NavigationToolbar component.
NavigationToolbar appears on the left side of the screen where navigation components
usually expected. You can implement awesome navigation if combine NavigationToolbar with a
material NavigationDrawer.

## Before you start

Before you start using **furdroid** please make sure you have Android artifacts 'android:android' and
'com.android.support' in your local Maven repository. If you don't please visit
[maven-android-sdk-deployer](https://github.com/simpligility/maven-android-sdk-deployer)
project and follow the instruction.

## Maven Dependency

```xml
<dependency>
  <groupId>systems.furdei</groupId>
  <artifactId>furdroid-navigationtoolbar</artifactId>
  <version>${project.version}</version>
</dependency>
```

## Gradle Dependency

```groovy
dependencies {
  compile 'systems.furdei:furdroid-navigationtoolbar:${project.version}'
}

```

## Description

Navigation Toolbar appears on left edge of the screen and shows navigation items only
By default these are icons, so Navigation Toolbar looks like Action Bar.
The main difference is that it is on the left of the screen where navigation drawer usually
appears. So it should be much more convenient to a user to look for navigation buttons
at the Navigation Toolbar instead of Action Bar.

When using a Navigation Toolbar in your project you should put the content of your activity
inside the navigation bar as shown here:

```xml
<!-- Left-side toolbar with quick navigation buttons -->
<com.furdei.furdroid.navigationtoolbar.NavigationToolbar
     android:id="@+id/navigation_toolbar"
     android:layout_width="match_parent"
     android:layout_height="match_parent" >

     <!-- Main layout -->
     <FrameLayout
         android:id="@+id/activity_content"
         android:layout_width="match_parent"
         android:layout_height="match_parent">
     </FrameLayout>

</com.furdei.furdroid.navigationtoolbar.NavigationToolbar>
```

### Populating a toolbar

Navigation Toolbar is populated with items inflated from menu resource. Call
setMenuResId(int) to specify a menu for inflating items. You can also use a
navigationToolbarMenu XML property to specify a menu right in the layout.

### Responding to user events

When user clicks a Navigation Toolbar button, the default implementation
calls onOptionsItemSelected(MenuItem) method and
allows developer to react to navigation events in the same manner he would process Action Bar
events. You can change this behaviour by calling setNavigationToolbarListener(NavigationToolbarListener) method
and providing your own implementation of NavigationToolbarListener
interface. The default implementation is OptionsMenuNavigationToolbarListener class.

OptionsMenuNavigationToolbarListener also allows you to receive clicks on Home/Up button when user clicks an
Overflow button. This is the default behaviour, but you can change it by calling
setClickHomeUpForOverflowButton(boolean) method.

## furdroid

**Furdroid-components** is distributed as a part of [furdroid](https://github.com/furdei/furdroid) project.
Follow [this link](https://github.com/furdei/furdroid) to find more useful visual components, widgets and database
tools by [furdei.systems](http://www.furdei.systems).
