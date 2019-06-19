# Chapter 2.c: Localization

Adapting to a user's language is already important for desktop applications, but it is critical for the web since usually the whole world will have access to a site.

Vaadin Flow already comes with a set of features to fulfill this purpose, but Cotton is able to easily load translations into these features and to retrieve from them from every point in the application.

## Defining Resource Files

When defining resource files, the file extension is not of any concern, resource contents will be read as text either way. **_Cotton_** only needs to know the file's char set, so it will be decoded correctly.

Whats more important is the file's name. When loading a translation resource, **_Cotton_** expects one equally prefixed file for every language, with the **ISO3** language code as suffix. 

So for example, if the resource is a _.txt_ named "_myResource_" and it is available in english, german and frech, **_Cotton_** expects to find 3 files:
- myResource_eng.txt
- myResource_deu.txt
- myResource_fra.txt

**_Cotton_** will ensure the validity of language files:
- Two files of the same resource (for example "_myResource_eng_" and "_myResource_fra_") have to contain the exact same set of translation identifiers
- Two files of different resources (for example "_myAResource_" and "_myBResource_") are not allowed to contain any translation identifier of their significant other

In the sense of separated concerns, we will now create 4 files total; a technical and a business resource, both in english and in french:

_technical_eng.i18n_ :
````properties
identifier.a = dynamic: {identifier.c}
identifier.b = static: {identifier.c}
````

_technical_fra.i18n_ :
````properties
identifier.a = dynamique: {identifier.c}
identifier.b = statique: {identifier.c}
````

_business_eng.i18n_ :
````properties
identifier.c = That's Awesome!
````

_business_fra.i18n_ :
````properties
identifier.c = Tres Magnifique!
````

As you can see, the translation identifiers of both resources are the same, their value is simply different in relation to the language. 

Also, you can see that we are using a business identifier in a placeholder in the value of a technical identifier; the resources might be separated, but internally **_Cotton_** will create a pool of translations to pick from, so translations can use each other like regular placeholders.

## Loading Resource Files

In the correct **_Blueprint_** simply create two _@Define_-annotated method returning a list of **_SingletonAllocation_**, one for each resource:

````java
@Define
public List<SingletonAllocation> defineTechnicalLocalizations() {
    return CottonEnvironment.forLocalization("technical", "i18n",
        Charset.forName("UTF-8"), Locale.ENGLISH, Locale.FRENCH);
}

@Define
public List<SingletonAllocation> defineTranslationLocalizations() {
    return CottonEnvironment.forLocalization("translations", "i18n",
        Charset.forName("UTF-8"), Locale.ENGLISH, Locale.FRENCH);
}
````

The methods will return one allocation for each file, which will be picked up by **_Cotton_**'s localizer and loaded automatically.

## Translating Identifiers

For retrieving a translation based on an identifier either one of two ways can be used:
- The **_Vaadin_** way, which works by calling the _getTranslation()_ method of a **_Component_** instance
- The **_Cotton_** way, by calling the **_WebEnv_** statically

Both ways provide the same translations, its just that in some occasions no **_Component_** instance might be accessible, in which case the static access via **_WebEnv_** comes in handy.

To demonstrate both variations, we create a view that uses both in one **_Label_** each:

````java
@Route("demo")
public class DemoView extends Div {

    public DemoView() {
        add(new HorizontalLayout(
                new Label(getTranslation("identifier.a")),
                new Label(WebEnv.getTranslation("identifier.b"))));
    }
}
````

The identifiers will both be translated agains the technical resource, which in turn will receive the value for their placeholder from the business resource.