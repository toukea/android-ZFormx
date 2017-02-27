# IstatForms
AN Android  library that make Forms manipulation very easy
fill form from view,  flow form into view, proceed form validation
# Handle Form Entity (Create,read,... )
Form object is as simple HashMap Object. so you can put name value pair corresponding at form's fieldName, values. 
```java
Form form = new Form();
form.put("firstName","Toto");
form.put("lastName","Titi");
form.put("years",25);
form.put("someFieldName",mObject);
```
It is also possible to retrieve inserted value from your form Object 
Form object is as simple HashMap Object. so you can put name value pair corresponding at form's fieldName, values. 
```java
Object mObject = form.get("someFieldName");
int years = form.optInt("years");
String lastName = form.optString("lastName");
String firstName = form.optString("firstName");
```

# From Form to Object and vice versa.
It is possible to create Form from object or Object from form. to show that.
i am creating some User.class with default constructor(necessary)
```java
class User{
    String firstName;
    String lastName;
    int years;
    Object someFieldName:
}
```
Now, i can create an empty form from User.class.
```java
Form form = Form.fromClass(User.class);
```
It is also possible to create not empty Form from an non empty Object instance.
```java
User user = new User();
user.firstName = "Toto";
user.lastName = "Titi";
user.years = 25;
user.someFieldName = mObject;
Form form = Form.fromObject(user);
```
Form Entity provide possibility to turn them into object.
```java
User user = form.as(User.class);
```
# Flow Form Entity content into created view.
With <b>ZFormx</b> library, you can easily fill a view by flowing form content into.
For this, you should insure that your view's Tag have same name with your form's field.
So, let create some Xml layout View.
```xml
<?xml version = "1.0" encoding = "utf-8"?>
<LinearLayout xmlns:android = "http://schemas.android.com/apk/res/android"
    android:id = "@+id/form_layout"
    android:layout_width = "match_parent"
    android:layout_height = "match_parent">
    <EditText
        android:layout_width = "wrap_content"
        android:layout_height = "wrap_content"
        android:hint = "First Name"
        android:tag = "firstName..." />
    <EditText
           android:layout_width = "wrap_content"
           android:layout_height = "wrap_content"
           android:hint = "Last Name"
           android:tag = "lastName..." />
    <EditText
           android:layout_width = "wrap_content"
           android:layout_height = "wrap_content"
           android:hint = "Years..."
           android:tag = "years" />
</LinearLayout>    
```
Now , from my Activity, i can flow <b>Form</b> Entity into that created View which has id = R.id.form_layout.
```java
View mFormView = findViewById(R.id.form_layout);

FormFlower.use(mForm) //create a flower using a specific Form Entity (mForm)
            /*setFillAccessibleOnly spécify if you want to flow enabled view Only 
            (desabled view and not fowussable view would be ignored.)*/
          .setFlowAccessibleOnly(false) 
          .flowInto(mFormView); //flow the form entity into the formView
```
# Flow Form Entity content into Object instance.
Documentation in progress... :-)

# Fill Form Entity content from created view.
```java
View mFormView = findViewById(R.id.form_layout);
FormFiller.use(mForm) //create a filler using a specific Form Entity (mForm)
            /*setFillAccessibleOnly spécify if you want to flow enabled view Only
            (desabled view and not fowussable view would be ignored.)*/
          .setFillAccessibleOnly(false) 
          .fillWith(mFormView); //fill the form with view values.
```

# Fill Form Entity content from Object instance.
Documentation in progress... :-)

# Working with Form Validation
Documentation in progress... :-)

Usage
-----
Just add the dependency to your `build.gradle`:

```groovy
dependencies {
   compile 'istat.android.freedev.forms:istat-zformx:1.1.3'
}
```

minSdkVersion  =  10
------------------
Library is compatible with Android 2.3 and newer.

Download
--------
add the dependency to your pom.xml:

```xml
<dependency>
  <groupId>istat.android.freedev.forms</groupId>
  <artifactId>istat-zformx</artifactId>
  <version>1.1.3</version>
  <type>pom</type>
</dependency>
```

